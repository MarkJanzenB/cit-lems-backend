package com.capstone.LEMS.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import com.capstone.LEMS.Entity.*;
import com.capstone.LEMS.Repository.BatchResupplyRepository;
import com.capstone.LEMS.Repository.BorrowCartRepository;
import com.capstone.LEMS.Repository.BorrowItemRepository;
import com.capstone.LEMS.Repository.InventoryRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.ManufacturerRepository;
import com.capstone.LEMS.Repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {
	private static final Logger log = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	ItemRepository itemrepo;

//  @Autowired
//  IdCounterService idcountserv; // This is correctly commented out and won't be used for Item unique_ids

	@Autowired
	UserRepository userrepo;

	@Autowired
	InventoryRepository invrepo;

	@Autowired
	BorrowCartRepository borrowcartrepo;

	@Autowired
	BorrowItemRepository borrowitemrepo;

	@Autowired
	ManufacturerRepository mrepo;

	@Autowired
	BatchResupplyRepository brrepo;

	@Autowired
	BatchResupplyService brserv;

	@SuppressWarnings({ "unchecked", "null" })
	@Transactional
	public ResponseEntity<?> AddItem(Map<String, Object> itemsToAdd, int bulkSize) {
		// 1. Extracting data from the request Map
		String itemName = (String) itemsToAdd.get("item_name");
		int inventoryId = (int) itemsToAdd.get("inventory_id");
		String category = (String) itemsToAdd.get("category");
		List<String> uniqueIds = (List<String>) itemsToAdd.get("unique_ids"); // Will be null for consumables
		// List<ItemEntity> itemsToSave = new ArrayList<>(); // <--- OLD: Renamed for clarity
		List<ItemEntity> itemsToProcess = new ArrayList<>(); // <--- NEW: List to hold items before initial save

		int quantityFromFrontend = (int) itemsToAdd.get("quantity"); // This is the 'quantity' from frontend payload
		String expiryDateStr = (String) itemsToAdd.get("expiry_date");
		LocalDate expiryDate = (expiryDateStr != null && !expiryDateStr.isEmpty()) ? LocalDate.parse(expiryDateStr) : null;
		String variant = (String) itemsToAdd.get("variant");
		Integer manufacturerId = (Integer) itemsToAdd.get("manufacturer_id");

		// Fetch related entities
		ManufacturerEntity manufacturer = null;
		if (manufacturerId != null) {
			manufacturer = mrepo.findById(manufacturerId).orElse(null);
		}

		UserEntity user = userrepo.findById((int) itemsToAdd.get("uid")).orElse(null);
		if (user == null) {
			log.error("AddItem failed: User not found for UID {}", itemsToAdd.get("uid"));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found for provided UID.");
		}

		InventoryEntity inventory = invrepo.findById(inventoryId).orElse(null);
		if (inventory == null) {
			log.error("AddItem failed: Inventory ID {} does not exist.", inventoryId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory ID: " + inventoryId + " does not exist.");
		}

		BatchResupplyEntity batchResupply = new BatchResupplyEntity();
		batchResupply.setDateResupply(LocalDate.now());
		batchResupply.setAddedBy(user);
		batchResupply = brserv.addBatchResupply(batchResupply);

		// 2. Initial Validations
		if (variant == null || variant.trim().isEmpty()) {
			log.error("AddItem failed: Variant field is blank or null.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Variant field should not be blank.");
		}

		// 3. Conditional Logic based on Item Category
		if (category != null && category.equalsIgnoreCase("Consumables")) {
			if (quantityFromFrontend <= 0) { // For consumables, quantityFromFrontend is amountToAdd
				log.error("AddItem failed (Consumable): Quantity to add must be greater than 0.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity to add for consumables must be greater than 0.");
			}

			Optional<ItemEntity> existingConsumableItemOptional = itemrepo.findByInventory_InventoryIdAndVariantAndExpiryDateAndIsDeletedFalse(
					inventoryId, variant, expiryDate
			);

			if (existingConsumableItemOptional.isPresent()) {
				log.info("Found existing consumable batch for inventoryId: {}, variant: {}, expiry: {}. Increasing quantity.",
						inventoryId, variant, expiryDate);
				ItemEntity existingItem = existingConsumableItemOptional.get();
				existingItem.setQuantity(existingItem.getQuantity() + quantityFromFrontend);
				existingItem.setBatchResupply(batchResupply);
				itemsToProcess.add(existingItem); // Add to list for final save
			} else {
				log.info("Creating new consumable batch for inventoryId: {}, variant: {}, expiry: {}.",
						inventoryId, variant, expiryDate);
				ItemEntity newItem = new ItemEntity();
				newItem.setItemName(itemName);
				newItem.setInventory(inventory);
				newItem.setStatus("Available");
				newItem.setAutoUid(true); // Consumables are auto-UID (no individual serial)
				newItem.setUniqueId(null); // Consumables typically don't have individual unique IDs
				newItem.setQuantity(quantityFromFrontend);
				newItem.setExpiryDate(expiryDate);
				newItem.setVariant(variant);
				newItem.setBatchResupply(batchResupply);
				if (manufacturer != null) {
					newItem.setManufacturer(manufacturer);
				}
				newItem.setIsDeleted(false); // Ensure new items are not deleted
				itemsToProcess.add(newItem); // Add to list for final save
			}
		} else {
			// Non-Consumable Item Logic: Create individual ItemEntities for each unit
			if (bulkSize <= 0) { // bulkSize from frontend is amountToAdd for non-consumables
				log.error("AddItem failed (Non-Consumable): Invalid bulk size {}. It must be greater than zero.", bulkSize);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid bulk size for non-consumables. It must be greater than zero.");
			}

			// Check for existing custom unique IDs to prevent duplicates BEFORE creating new items
			List<String> providedUniqueIds = new ArrayList<>();
			if (uniqueIds != null && !uniqueIds.isEmpty()) {
				providedUniqueIds = uniqueIds.stream()
						.filter(id -> id != null && !id.trim().isEmpty())
						.map(String::trim)
						.collect(Collectors.toList());
			}

			if (!providedUniqueIds.isEmpty()) {
				List<ItemEntity> foundItems = itemrepo.findByUniqueIdInAndIsDeletedFalse(providedUniqueIds);
				if (!foundItems.isEmpty()) {
					List<String> foundUniqueIds = foundItems.stream()
							.map(ItemEntity::getUniqueId)
							.collect(Collectors.toList());
					Map<String, Object> response = new HashMap<>();
					response.put("message", "The following Unique IDs already exist:");
					response.put("unique_ids", foundUniqueIds);
					log.error("AddItem failed (Non-Consumable): Duplicate unique IDs found: {}", foundUniqueIds);
					return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
				}
			}

			// Create new ItemEntities for each non-consumable unit
			for (int i = 0; i < bulkSize; i++) {
				ItemEntity newItem = new ItemEntity();
				newItem.setItemName(itemName);
				newItem.setInventory(inventory);
				newItem.setStatus("Available");
				newItem.setVariant(variant);
				newItem.setQuantity(1); // Each non-consumable ItemEntity represents one unit
				newItem.setBatchResupply(batchResupply);
				if (manufacturer != null) {
					newItem.setManufacturer(manufacturer);
				}
				newItem.setIsDeleted(false); // Ensure new items are not deleted

				// Assign unique ID: either custom or flag for auto-generation (will be populated AFTER first save)
				if (i < providedUniqueIds.size()) {
					newItem.setUniqueId(providedUniqueIds.get(i));
					newItem.setAutoUid(false); // Manually provided UID
				} else {
					newItem.setAutoUid(true); // <--- NEW: Flag for auto-generation after save
					newItem.setUniqueId(null); // <--- NEW: Temporarily null, will be set after getting itemId
				}
				itemsToProcess.add(newItem); // <--- Add to `itemsToProcess` list
			}
		}

		// 4. Save all prepared ItemEntities to the database
		// This first save will assign itemIds to new entities due to GenerationType.IDENTITY
		List<ItemEntity> savedItems = itemrepo.saveAll(itemsToProcess);
		log.info("Successfully performed initial save/update for {} items.", savedItems.size());

		// 5. Post-save processing for auto-generated unique_ids (NON-CONSUMABLES ONLY)
		// This loop only runs for items that were just inserted AND needed auto-UIDs
		List<ItemEntity> itemsToUpdateWithGeneratedUID = new ArrayList<>();
		for (ItemEntity item : savedItems) {
			// Check if it's an auto-UID item and its uniqueId is still null (meaning it was just inserted)
			if (item.isAutoUid() && item.getUniqueId() == null && item.getItemId() != 0) { // item.getItemId() != 0 checks if it got an ID
				// Generate the prefix based on item name (as per your existing logic)
				String prefix = item.getItemName().length() >= 2 ? item.getItemName().substring(0, 2).toUpperCase() : item.getItemName().toUpperCase();
				if (item.getItemName().length() > 0) {
					prefix += item.getItemName().substring(item.getItemName().length() - 1).toUpperCase();
				} else {
					prefix = "XX";
				}

				// <--- CRUCIAL CHANGE: Use the newly assigned itemId as the sequential number
				String uniqueId = prefix + String.format("%04d", item.getItemId()); // Format to 4 digits
				item.setUniqueId(uniqueId);
				itemsToUpdateWithGeneratedUID.add(item);
			}
		}

		// <--- NEW: Perform a second saveAll to update items with their generated unique_ids
		if (!itemsToUpdateWithGeneratedUID.isEmpty()) {
			itemrepo.saveAll(itemsToUpdateWithGeneratedUID);
			log.info("Successfully updated {} items with auto-generated unique IDs.", itemsToUpdateWithGeneratedUID.size());
		}

		// 6. Update the overall InventoryEntity quantity (CRUCIAL for accurate stock count)
		Integer totalQuantityInInventory = itemrepo.sumQuantityByInventoryId(inventoryId);
		if (totalQuantityInInventory == null) totalQuantityInInventory = 0;

		inventory.setQuantity(totalQuantityInInventory);
		inventory.setStatus(totalQuantityInInventory > 0 ? "Available" : "Out of stock");
		invrepo.save(inventory);
		log.info("Inventory '{}' (ID: {}) quantity updated to: {}. Status set to: {}",
				inventory.getName(), inventory.getInventoryId(), inventory.getQuantity(), inventory.getStatus());

		// 7. Return response
		// The `savedItems` list will now contain the fully updated items (with unique_id set for auto-generated ones)
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItems);
	}


	// --- Other methods of ItemService follow below (no changes needed) ---
	// OwO what is this comment for?

	public ResponseEntity<?> updateItems(String itemToEdit, ItemEntity newItemDetails){
		log.info("Starting updateItems for itemToEdit: {}", itemToEdit);

		if(newItemDetails.getItemName() == null || newItemDetails.getItemName().trim().isEmpty()) {
			log.warn("Validation failed: newItemDetails.getItemName() is blank or null");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST) // 400
					.body("Blank item name is not allowed.");
		}

		log.info("Fetching items with name: {}", itemToEdit);
		// Use findByItemNameAndIsDeletedFalse
		List<ItemEntity> items = itemrepo.findByItemNameAndIsDeletedFalse(itemToEdit);

		log.info("Updating items with new name: {}", newItemDetails.getItemName());
		items.forEach(item -> {
			item.setItemName(newItemDetails.getItemName());
		});
		log.info("Saving updated items to the database");
		List<ItemEntity> updatedItems = itemrepo.saveAll(items);
		log.info("Successfully updated {} items", updatedItems.size());
		return ResponseEntity
				.status(HttpStatus.OK) // 200
				.body(updatedItems);
	}

	/*
	 * Deletes items only based on bulk size
	 * not specifically
	 * */
	public ResponseEntity<?> deleteItems(int bulkSize, ItemEntity itemsToDelete){
		if(itemsToDelete.getItemName() == null || itemsToDelete.getItemName().trim().isEmpty()) {
			log.warn("Validation failed: itemsToDelete.getItemName() is blank or null");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST) // 400
					.body("Blank item name is not allowed.");
		}

		log.info("Fetching items with name: {}", itemsToDelete.getItemName());
		// Use findByItemNameAndUserIsNullAndIsDeletedFalseOrderByItemIdDesc
		List<ItemEntity> items = itemrepo.findByItemNameAndUserIsNullAndIsDeletedFalseOrderByItemIdDesc(itemsToDelete.getItemName());

		if(items.isEmpty()) {
			log.warn("No items found with name: {}", itemsToDelete.getItemName());
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND) // 404
					.body("No items found with name: " + itemsToDelete.getItemName());
		}

		if(items.size() < bulkSize) {
			log.warn("Attempting to delete more items than available or attempting to delete items that are used by a group");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST) // 400
					.body("Cannot delete items more than the available or that some items are currently in used by a group");
		}

		List<ItemEntity> itemsToDeleteList = items.subList(0, bulkSize);
		log.info("Soft deleting {} items with name: {}", itemsToDeleteList.size(), itemsToDelete.getItemName());
		// Instead of deleteAll, iterate and set isDeleted to true
		itemsToDeleteList.forEach(item -> item.setIsDeleted(true));
		itemrepo.saveAll(itemsToDeleteList); // Save the updated status
		log.info("Successfully soft-deleted {} items", itemsToDeleteList.size());

		// After deletion, update the overall InventoryEntity quantity
		// Get the inventory entity associated with these items
		// Assuming all items in itemsToDeleteList belong to the same inventory (which they should)
		if (!itemsToDeleteList.isEmpty()) {
			int inventoryId = itemsToDeleteList.get(0).getInventory().getInventoryId();
			InventoryEntity inventory = invrepo.findById(inventoryId).orElse(null);
			if (inventory != null) {
				Integer totalQuantityInInventory = itemrepo.sumQuantityByInventoryId(inventoryId);
				if (totalQuantityInInventory == null) totalQuantityInInventory = 0;
				inventory.setQuantity(totalQuantityInInventory);
				inventory.setStatus(totalQuantityInInventory > 0 ? "Available" : "Out of stock");
				invrepo.save(inventory);
				log.info("Inventory {} quantity updated after soft deletion to: {}", inventory.getName(), inventory.getQuantity());
			}
		}


		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Successfully soft-deleted " + itemsToDeleteList.size() + " items.");
	}

	public List<ItemEntity> getAllItems(){
		// Use findByIsDeletedFalse() to only retrieve non-deleted items
		return itemrepo.findByIsDeletedFalse();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public ResponseEntity<?> borrowItem(Map<String, Object> request){
		int userID = (int) request.get("userID");
		List<Map<String,Object>> itemsRequest = (List<Map<String,Object>>) request.get("items");
		List<ItemEntity> itemsToBorrow = new ArrayList<>();
		UserEntity user = userrepo.findById(userID).orElse(null);
		int borrowCartID = (int) request.get("borrowCartID");
		BorrowCartEntity borrowCart = borrowcartrepo.findById(borrowCartID).orElse(null);

		if(user == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("User with ID " + userID + " could not be found");
		}

		for(Map<String, Object> itemReq: itemsRequest) {
			String itemName = (String) itemReq.get("itemName");
			int quantity = (int) itemReq.get("quantity");
			// Use findByItemNameAndStatusAndIsDeletedFalse
			List<ItemEntity> availableItems = itemrepo.findByItemNameAndStatusAndIsDeletedFalse(itemName, "Available");

			if (availableItems.size() < quantity) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Not enough available items for: " + itemName);
			}

			List<ItemEntity> itemsToAssign = availableItems.subList(0, quantity);
			itemsToAssign.forEach(item -> {
				item.setUser(user);
				item.setStatus("Preparing");
				item.setBorrowCart(borrowCart);
			});
			itemsToBorrow.addAll(itemsToAssign);
		}
		itemrepo.saveAll(itemsToBorrow);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(itemsToBorrow);
	}

	@Transactional
	public ResponseEntity<?> returnItem(List<Map<String, Object>> itemsRequest){
		List<ItemEntity> itemsToUpdate = new ArrayList<>();

		for (Map<String, Object> itemReq : itemsRequest) {
			String itemName = (String) itemReq.get("itemName");
			int borrowCartID = (int) itemReq.get("borrowCartID");
			String status = (String) itemReq.get("status");
			int quantity = (int) itemReq.get("quantity");

			// Use findByItemNameAndBorrowCart_IdAndIsDeletedFalse
			List<ItemEntity> items = itemrepo.findByItemNameAndBorrowCart_IdAndIsDeletedFalse(itemName, borrowCartID);

			if (items.isEmpty()) {
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body("Items with name " + itemName + "or with id " +borrowCartID + " could not be found.");
			}else if(quantity > items.size()) {
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("quantity is greater than the number of items the user borrowed");
			}

			List<ItemEntity> itemsToReturn = items.subList(0, quantity);

			itemsToReturn.forEach(item -> {
				item.setStatus(status);
				item.setUser(null);
				item.setBorrowCart(null);
				itemsToUpdate.add(item);
			});
		}

		itemrepo.saveAll(itemsToUpdate);

		// After returning items, update the overall InventoryEntity quantity for affected inventories
		// Group items by inventory ID to update each inventory's quantity correctly
		Map<Integer, List<ItemEntity>> itemsByInventory = itemsToUpdate.stream()
				.collect(Collectors.groupingBy(item -> item.getInventory().getInventoryId()));

		for (Map.Entry<Integer, List<ItemEntity>> entry : itemsByInventory.entrySet()) {
			Integer inventoryId = entry.getKey();
			InventoryEntity inventory = invrepo.findById(inventoryId).orElse(null);
			if (inventory != null) {
				Integer totalQuantityInInventory = itemrepo.sumQuantityByInventoryId(inventoryId);
				if (totalQuantityInInventory == null) totalQuantityInInventory = 0;
				inventory.setQuantity(totalQuantityInInventory);
				inventory.setStatus(totalQuantityInInventory > 0 ? "Available" : "Out of stock");
				invrepo.save(inventory);
				log.info("Inventory {} quantity updated after return to: {}", inventory.getName(), inventory.getQuantity());
			}
		}


		return ResponseEntity
				.status(HttpStatus.OK)
				.body(itemsToUpdate);
	}

	public ResponseEntity<?> getResupplyHistory(LocalDate resupplyDate, int uid){
		UserEntity user = userrepo.findById(uid).orElse(null);
		List<BatchResupplyEntity> batches = brrepo.findByDateResupplyAndAddedBy(resupplyDate, user);
		List<ItemEntity> itemToSend = new ArrayList<>();

		for(int i = 0; i < batches.size(); i++) {
			// Changed to findByBatchResupply to include all items regardless of isDeleted status
			List<ItemEntity> items = itemrepo.findByBatchResupply(batches.get(i));
			itemToSend.addAll(items);
		}

		Map<String, List<ItemEntity>> groupedItems = itemToSend.stream()
				.collect(Collectors.groupingBy(ItemEntity::getItemName));

		List<Map<String, Object>> response = new ArrayList<>();
		int index = 1;
		for(Map.Entry<String, List<ItemEntity>> entry : groupedItems.entrySet()) {
			String itemName = entry.getKey();
			List<ItemEntity> itemList = entry.getValue();

			Map<String, Object> itemSummary = new HashMap<>();
			itemSummary.put("id", index++);
			itemSummary.put("name", itemName);
			// For consumables, sum the quantities within the group
			// For non-consumables, count the items (each has quantity 1)
			if (!itemList.isEmpty() && itemList.get(0).getInventory().getItemCategory().getCategoryName().equalsIgnoreCase("Consumables")) {
				itemSummary.put("quantity", itemList.stream().mapToInt(ItemEntity::getQuantity).sum());
			} else {
				itemSummary.put("quantity", itemList.size());
			}


			List<Map<String, Object>> variants = itemList.stream().map(item -> {
				Map<String, Object> variant = new HashMap<>();
				variant.put("id", item.getItemId());
				variant.put("name", item.getItemName());
				variant.put("serialNumber", item.getUniqueId()); // This will now reflect the generated ID
				variant.put("quantity", item.getQuantity()); // Include quantity for consumables
				// Add isDeleted status to the variant details for history purposes
				variant.put("isDeleted", item.getIsDeleted());
				return variant;
			}).collect(Collectors.toList());

			itemSummary.put("variants", variants);
			response.add(itemSummary);
		}

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(response);
	}

	public ResponseEntity<?> getListOfUniqueIDs(String itemName, String category){
		// Use findByItemNameAndIsDeletedFalse
		List<ItemEntity> items = itemrepo.findByItemNameAndIsDeletedFalse(itemName);
		List<ItemEntity> availableItems = items.stream()
				.filter(item -> "Available".equals(item.getStatus()))
				.collect(Collectors.toList());

		if(category != null && !category.isBlank() && !category.isEmpty() && category.equalsIgnoreCase("Consumables")) {
			// For consumables, you might want to return details of available batches (variant, quantity, expiry)
			// instead of just the whole entity.
			List<Map<String, Object>> consumableBatches = availableItems.stream()
					.map(item -> {
						Map<String, Object> batchDetails = new HashMap<>();
						batchDetails.put("itemId", item.getItemId()); // Unique ID of the batch record
						batchDetails.put("variant", item.getVariant());
						batchDetails.put("quantity", item.getQuantity());
						batchDetails.put("expiryDate", item.getExpiryDate() != null ? item.getExpiryDate().toString() : null);
						return batchDetails;
					})
					.collect(Collectors.toList());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(consumableBatches);
		}else {
			// For non-consumables, return auto-UIDs as well
			// This previously filtered by !item.isAutoUid(), now it will include them
			List<String> uniqueIds = availableItems.stream()
					.map(ItemEntity::getUniqueId) // <--- Modified: now maps all unique_ids, whether custom or auto-generated
					.filter(id -> id != null && !id.trim().isEmpty()) // <--- Ensure non-null/empty UIDs are returned
					.collect(Collectors.toList());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(uniqueIds);

		}
	}

	public ResponseEntity<?> findByPreparingItemIds(List<Integer> preparingItemIds){
		// Use findByPreparingItem_IdInAndIsDeletedFalse
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(itemrepo.findByPreparingItem_IdInAndIsDeletedFalse(preparingItemIds));
	}

	public List<String> getAvailableVariants(String itemName) {
		// Ensure the query only fetches non-deleted items
		return itemrepo.findVariantsByItemNameAndStatus(itemName, "Available").stream()
				.filter(variant -> variant != null && !variant.isEmpty()) // Filter out null or empty variants
				.collect(Collectors.toList());
	}

	public List<Map<String, Object>> getAvailableVariantsWithQuantities(String itemName, String categoryName) {
		log.info("Fetching variants for item: {}, category: {}", itemName, categoryName);

		if (categoryName != null && categoryName.equalsIgnoreCase("Consumables")) {
			// Ensure the query only fetches non-deleted items
			List<Object[]> results = itemrepo.findConsumableVariantQuantities(itemName, "Available");
			log.info("Consumable variants found: {}", results.size());
			return results.stream()
					.map(result -> {
						Map<String, Object> map = new java.util.HashMap<>();
						map.put("variantName", result[0]);
						map.put("quantity", result[1]);
						return map;
					})
					.collect(Collectors.toList());
		} else {
			// Logic for Non-Consumables
			// Ensure the query only fetches non-deleted items
			List<String> distinctVariants = itemrepo.findVariantsByItemNameAndStatus(itemName, "Available");
			log.info("Non-consumable distinct variants found: {}", distinctVariants.size());

			return distinctVariants.stream()
					.map(variant -> {
						Map<String, Object> map = new java.util.HashMap<>();
						map.put("variantName", variant);

						// --- THIS IS THE CRITICAL PART FOR NON-CONSUMABLES' QUANTITY ---
						// We need to count individual ItemEntity records that match the itemName,
						// are 'Available', and have this specific 'variant'.
						// The `findByItemNameAndStatusAndIsDeletedFalse` returns a List<ItemEntity>.
						// We then filter that list by variant. This is the correct approach.

						List<ItemEntity> availableItemsForVariant = itemrepo.findByItemNameAndStatusAndIsDeletedFalse(itemName, "Available")
								.stream()
								.filter(item -> variant.equals(item.getVariant())) // Ensure variant is not null on item
								.collect(Collectors.toList());

						map.put("quantity", availableItemsForVariant.size()); // .size() is valid on List
						return map;
					})
					.collect(Collectors.toList());
		}
	}

	public ResponseEntity<?> getItemsByName(String itemName, String status){
		// Use findByItemNameAndStatusAndIsDeletedFalse
		List<ItemEntity> availableItems = itemrepo.findByItemNameAndStatusAndIsDeletedFalse(itemName, status);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(availableItems);
	}

	public ResponseEntity<?> deleteSpecificItems(List<ItemEntity> itemsToDel){
		// Instead of deleteAll, iterate and set isDeleted to true
		itemsToDel.forEach(item -> item.setIsDeleted(true));
		itemrepo.saveAll(itemsToDel); // Save the updated status

		/**
		 * After soft deletion, update the overall InventoryEntity quantity
		 * */
		if (!itemsToDel.isEmpty()) {
			int inventoryId = itemsToDel.get(0).getInventory().getInventoryId();
			InventoryEntity inventory = invrepo.findById(inventoryId).orElse(null);
			if (inventory != null) {
				// Recalculate total quantity from non-deleted items only
				Integer totalQuantityInInventory = itemrepo.sumQuantityByInventoryId(inventoryId);
				if (totalQuantityInInventory == null) totalQuantityInInventory = 0;
				inventory.setQuantity(totalQuantityInInventory);
				inventory.setStatus(totalQuantityInInventory > 0 ? "Available" : "Out of stock");
				invrepo.save(inventory);
			}
		}

		return ResponseEntity
				.status(HttpStatus.OK)
				.body(itemsToDel);
	}
}
