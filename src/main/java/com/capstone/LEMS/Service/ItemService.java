// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Service/ItemService.java
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
import com.capstone.LEMS.Repository.BatchPulloutRepository;

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

	@Autowired
	BatchPulloutRepository batchPulloutRepo;


	@SuppressWarnings({ "unchecked", "null" })
	@Transactional
	public ResponseEntity<?> AddItem(Map<String, Object> itemsToAdd, int bulkSize) {
		String itemName = (String) itemsToAdd.get("item_name");
		int inventoryId = (int) itemsToAdd.get("inventory_id");
		String category = (String) itemsToAdd.get("category");
		List<String> uniqueIds = (List<String>) itemsToAdd.get("unique_ids");
		List<ItemEntity> itemsToProcess = new ArrayList<>();

		int quantityFromFrontend = (int) itemsToAdd.get("quantity");
		String expiryDateStr = (String) itemsToAdd.get("expiry_date");
		LocalDate expiryDate = (expiryDateStr != null && !expiryDateStr.isEmpty()) ? LocalDate.parse(expiryDateStr) : null;
		String variant = (String) itemsToAdd.get("variant");
		Integer manufacturerId = (Integer) itemsToAdd.get("manufacturer_id");

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

		if (variant == null || variant.trim().isEmpty()) {
			log.error("AddItem failed: Variant field is blank or null.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Variant field should not be blank.");
		}

		if (category != null && category.equalsIgnoreCase("Consumables")) {
			if (quantityFromFrontend <= 0) {
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
				itemsToProcess.add(existingItem);
			} else {
				log.info("Creating new consumable batch for inventoryId: {}, variant: {}, expiry: {}.",
						inventoryId, variant, expiryDate);
				ItemEntity newItem = new ItemEntity();
				newItem.setItemName(itemName);
				newItem.setInventory(inventory);
				newItem.setStatus("Available");
				newItem.setAutoUid(true);
				newItem.setUniqueId(null);
				newItem.setQuantity(quantityFromFrontend);
				newItem.setExpiryDate(expiryDate);
				newItem.setVariant(variant);
				newItem.setBatchResupply(batchResupply);
				if (manufacturer != null) {
					newItem.setManufacturer(manufacturer);
				}
				newItem.setIsDeleted(false);
				itemsToProcess.add(newItem);
			}
		} else {
			if (bulkSize <= 0) {
				log.error("AddItem failed (Non-Consumable): Invalid bulk size {}. It must be greater than zero.", bulkSize);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid bulk size for non-consumables. It must be greater than zero.");
			}

			List<String> providedUniqueIds = new ArrayList<>();
			if (uniqueIds != null && uniqueIds.isEmpty()) {
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

			for (int i = 0; i < bulkSize; i++) {
				ItemEntity newItem = new ItemEntity();
				newItem.setItemName(itemName);
				newItem.setInventory(inventory);
				newItem.setStatus("Available");
				newItem.setVariant(variant);
				newItem.setQuantity(1);
				newItem.setBatchResupply(batchResupply);
				if (manufacturer != null) {
					newItem.setManufacturer(manufacturer);
				}
				newItem.setIsDeleted(false);

				if (i < providedUniqueIds.size()) {
					newItem.setUniqueId(providedUniqueIds.get(i));
					newItem.setAutoUid(false);
				} else {
					newItem.setAutoUid(true);
					newItem.setUniqueId(null);
				}
				itemsToProcess.add(newItem);
			}
		}

		List<ItemEntity> savedItems = itemrepo.saveAll(itemsToProcess);
		log.info("Successfully performed initial save/update for {} items.", savedItems.size());

		List<ItemEntity> itemsToUpdateWithGeneratedUID = new ArrayList<>();
		for (ItemEntity item : savedItems) {
			if (item.isAutoUid() && item.getUniqueId() == null && item.getItemId() != 0) {
				String prefix = item.getItemName().length() >= 2 ? item.getItemName().substring(0, 2).toUpperCase() : item.getItemName().toUpperCase();
				if (item.getItemName().length() > 0) {
					prefix += item.getItemName().substring(item.getItemName().length() - 1).toUpperCase();
				} else {
					prefix = "XX";
				}

				String uniqueId = prefix + String.format("%04d", item.getItemId());
				item.setUniqueId(uniqueId);
				itemsToUpdateWithGeneratedUID.add(item);
			}
		}

		if (!itemsToUpdateWithGeneratedUID.isEmpty()) {
			itemrepo.saveAll(itemsToUpdateWithGeneratedUID);
			log.info("Successfully updated {} items with auto-generated unique IDs.", itemsToUpdateWithGeneratedUID.size());
		}

		Integer totalQuantityInInventory = itemrepo.sumQuantityByInventoryId(inventoryId);
		if (totalQuantityInInventory == null) totalQuantityInInventory = 0;

		inventory.setQuantity(totalQuantityInInventory);
		inventory.setStatus(totalQuantityInInventory > 0 ? "Available" : "Out of stock");
		invrepo.save(inventory);
		log.info("Inventory '{}' (ID: {}) quantity updated to: {}. Status set to: {}",
				inventory.getName(), inventory.getInventoryId(), inventory.getQuantity(), inventory.getStatus());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedItems);
	}


	public ResponseEntity<?> updateItems(String itemToEdit, ItemEntity newItemDetails){
		log.info("Starting updateItems for itemToEdit: {}", itemToEdit);

		if(newItemDetails.getItemName() == null || newItemDetails.getItemName().trim().isEmpty()) {
			log.warn("Validation failed: newItemDetails.getItemName() is blank or null");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Blank item name is not allowed.");
		}

		log.info("Fetching items with name: {}", itemToEdit);
		List<ItemEntity> items = itemrepo.findByItemNameAndIsDeletedFalse(itemToEdit);

		log.info("Updating items with new name: {}", newItemDetails.getItemName());
		items.forEach(item -> {
			item.setItemName(newItemDetails.getItemName());
		});
		log.info("Saving updated items to the database");
		List<ItemEntity> updatedItems = itemrepo.saveAll(items);
		log.info("Successfully updated {} items", updatedItems.size());
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(updatedItems);
	}

	/*
	 * This method is for deleting items based on a bulk size and item name.
	 * It restores the original functionality for the /deleteitems endpoint.
	 */
	@Transactional
	public ResponseEntity<?> deleteItems(int bulkSize, ItemEntity itemsToDelete, int userId){
		if(itemsToDelete.getItemName() == null || itemsToDelete.getItemName().trim().isEmpty()) {
			log.warn("Validation failed: itemsToDelete.getItemName() is blank or null");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Blank item name is not allowed.");
		}

		log.info("Fetching items with name: {}", itemsToDelete.getItemName());
		List<ItemEntity> items = itemrepo.findByItemNameAndUserIsNullAndIsDeletedFalseOrderByItemIdDesc(itemsToDelete.getItemName());

		if(items.isEmpty()) {
			log.warn("No items found with name: {}", itemsToDelete.getItemName());
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("No items found with name: " + itemsToDelete.getItemName());
		}

		if(items.size() < bulkSize) {
			log.warn("Attempting to delete more items than available or attempting to delete items that are used by a group");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Cannot delete items more than the available or that some items are currently in used by a group");
		}

		List<ItemEntity> itemsToDeleteList = items.subList(0, bulkSize);
		log.info("Soft deleting {} items with name: {}", itemsToDeleteList.size(), itemsToDelete.getItemName());
		itemsToDeleteList.forEach(item -> item.setIsDeleted(true));
		itemrepo.saveAll(itemsToDeleteList);
		log.info("Successfully soft-deleted {} items", itemsToDeleteList.size());

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

	/*
	 * This method is specifically for soft-deleting a list of individual ItemEntity objects
	 * and linking them to a BatchPulloutEntity. This is used by the /deletespecificitems endpoint.
	 */
	@Transactional
	public ResponseEntity<?> deleteSpecificItems(List<ItemEntity> itemsToDel, int userId){
		if(itemsToDel == null || itemsToDel.isEmpty()) {
			log.warn("Validation failed: itemsToDel list is null or empty");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Items to delete list cannot be empty.");
		}

		String itemName = itemsToDel.get(0).getItemName();
		int bulkSize = itemsToDel.size();

		log.info("Attempting to soft delete {} specific items with name: {} by userId: {}", bulkSize, itemName, userId);

		List<Integer> itemIdsToProcess = itemsToDel.stream()
				.map(ItemEntity::getItemId)
				.collect(Collectors.toList());
		List<ItemEntity> managedItemsToUpdate = itemrepo.findAllById(itemIdsToProcess);

		if (managedItemsToUpdate.size() != itemsToDel.size()) {
			log.warn("Mismatch between requested items and found items for deletion.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some items to delete were not found in the database.");
		}

		for (int i = 0; i < managedItemsToUpdate.size(); i++) {
			ItemEntity managedItem = managedItemsToUpdate.get(i);
			if (!"Available".equals(managedItem.getStatus()) || managedItem.getUser() != null) {
				log.warn("Attempt to delete item {} which is not available or is currently in use. Status: {}, User: {}", managedItem.getItemId(), managedItem.getStatus(), managedItem.getUser() != null ? managedItem.getUser().getUid() : "none");
				return ResponseEntity
						.status(HttpStatus.BAD_REQUEST)
						.body("Cannot delete items that are not available or are currently in use by a borrower. Item ID: " + managedItem.getItemId());
			}
		}


		UserEntity pulledBy = userrepo.findById(userId).orElse(null);
		if (pulledBy == null) {
			log.error("deleteSpecificItems failed: User not found for userId {}", userId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found for provided userId.");
		}

		BatchPulloutEntity batchPullout = new BatchPulloutEntity();
		batchPullout.setDatePullout(LocalDate.now());
		batchPullout.setPulledBy(pulledBy);
		batchPullout = batchPulloutRepo.save(batchPullout); // Save the new batch pullout here

		// NEW: Establish the relationship from the owning side (BatchPulloutEntity)
		// This should ensure the pullout_id is correctly set in the Item table
		for (ItemEntity item : managedItemsToUpdate) {
			item.setIsDeleted(true); // Mark as deleted
			batchPullout.addItem(item); // Add to the collection and set the back-reference (pullout_id)
		}
		// Saving the batchPullout again will persist the changes to its 'items' collection
		// due to CascadeType.ALL on BatchPulloutEntity's @OneToMany
		batchPulloutRepo.save(batchPullout); // Re-save the BatchPullout to cascade item updates

		log.info("Successfully soft-deleted {} items and linked to BatchPullout ID: {}", managedItemsToUpdate.size(), batchPullout.getPulloutId());

		if (!managedItemsToUpdate.isEmpty()) {
			int inventoryId = managedItemsToUpdate.get(0).getInventory().getInventoryId();
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
				.body("Successfully soft-deleted " + managedItemsToUpdate.size() + " items.");
	}


	public List<ItemEntity> getAllItems(){
		return itemrepo.findByIsDeletedFalse();
	}

	public List<ItemEntity> getDeletedItems(){
		return itemrepo.findByIsDeletedTrue();
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
			if (!itemList.isEmpty() && itemList.get(0).getInventory().getItemCategory().getCategoryName().equalsIgnoreCase("Consumables")) {
				itemSummary.put("quantity", itemList.stream().mapToInt(ItemEntity::getQuantity).sum());
			} else {
				itemSummary.put("quantity", itemList.size());
			}


			List<Map<String, Object>> variants = itemList.stream().map(item -> {
				Map<String, Object> variant = new HashMap<>();
				variant.put("id", item.getItemId());
				variant.put("name", item.getVariant());
				variant.put("serialNumber", item.getUniqueId());
				variant.put("quantity", item.getQuantity());
				variant.put("isDeleted", item.getIsDeleted());
				variant.put("categoryName", item.getInventory() != null && item.getInventory().getItemCategory() != null ? item.getInventory().getItemCategory().getCategoryName() : "N/A");
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
		List<ItemEntity> items = itemrepo.findByItemNameAndIsDeletedFalse(itemName);
		List<ItemEntity> availableItems = items.stream()
				.filter(item -> "Available".equals(item.getStatus()))
				.collect(Collectors.toList());

		if(category != null && !category.isBlank() && !category.isEmpty() && category.equalsIgnoreCase("Consumables")) {
			List<Map<String, Object>> consumableBatches = availableItems.stream()
					.map(item -> {
						Map<String, Object> batchDetails = new HashMap<>();
						batchDetails.put("itemId", item.getItemId());
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
			List<String> uniqueIds = availableItems.stream()
					.map(ItemEntity::getUniqueId)
					.filter(id -> id != null && !id.trim().isEmpty())
					.collect(Collectors.toList());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(uniqueIds);

		}
	}

	public ResponseEntity<?> findByPreparingItemIds(List<Integer> preparingItemIds){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(itemrepo.findByPreparingItem_IdInAndIsDeletedFalse(preparingItemIds));
	}

	public List<String> getAvailableVariants(String itemName) {
		return itemrepo.findVariantsByItemNameAndStatus(itemName, "Available").stream()
				.filter(variant -> variant != null && !variant.isEmpty())
				.collect(Collectors.toList());
	}

	public List<Map<String, Object>> getAvailableVariantsWithQuantities(String itemName, String categoryName) {
		log.info("Fetching variants for item: {}, category: {}", itemName, categoryName);

		if (categoryName != null && categoryName.equalsIgnoreCase("Consumables")) {
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
			List<String> distinctVariants = itemrepo.findVariantsByItemNameAndStatus(itemName, "Available");
			log.info("Non-consumable distinct variants found: {}", distinctVariants.size());

			return distinctVariants.stream()
					.map(variant -> {
						Map<String, Object> map = new java.util.HashMap<>();
						map.put("variantName", variant);

						List<ItemEntity> availableItemsForVariant = itemrepo.findByItemNameAndStatusAndIsDeletedFalse(itemName, "Available")
								.stream()
								.filter(item -> variant.equals(item.getVariant()))
								.collect(Collectors.toList());

						map.put("quantity", availableItemsForVariant.size());
						return map;
					})
					.collect(Collectors.toList());
		}
	}

	public ResponseEntity<?> getItemsByName(String itemName, String status){
		List<ItemEntity> availableItems = itemrepo.findByItemNameAndStatusAndIsDeletedFalse(itemName, status);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(availableItems);
	}

//	@Autowired
//	private ItemRepository itemRepo;

//	public List<ItemEntity> getItemsByStatus(String status) {
//		return itemRepo.findByStatusAndIsDeletedFalse(status);
//	}

	public List<ItemEntity> getItemsByStatus(String status, Integer userId) {
		if (userId != null) {
			// If userId is provided, filter by user
			return itemrepo.findByStatusAndUser_UserIdAndIsDeletedFalse(status, userId);
		}
		// Otherwise, return all items with the given status
		return itemrepo.findByStatusAndIsDeletedFalse(status);
	}

}