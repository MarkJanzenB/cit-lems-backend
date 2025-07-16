package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.*;
import com.capstone.LEMS.Repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PreparingItemService {

	@Autowired
	private PreparingItemRepository preparingItemRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeacherScheduleRepository teacherScheduleRepository;

	@Autowired
	private InventoryRepository invrepo;

	private static final Logger logger = LoggerFactory.getLogger(PreparingItemService.class);
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * UPDATED METHOD
	 * Adds an item to be prepared. This now links to the master inventory,
	 * checks for sufficient stock, and reduces the inventory count upon reservation.
	 */
	@Transactional
	public PreparingItemEntity addToPreparingItem(String instiId, String itemName, String categoryName, int quantity, String status) {
		// Step 1: Find the master inventory record for the item being requested.
		// Note: You may need a more specific finder method, e.g., findByItemNameAndCategoryName
		InventoryEntity inventory = invrepo.findByName(itemName)
				.orElseThrow(() -> new NoSuchElementException("Cannot prepare item. Inventory record not found for: " + itemName));
		// Step 2: Check if there is enough stock available in the inventory.
		if (inventory.getQuantity() < quantity) {
			throw new IllegalStateException("Not enough stock for " + itemName + ". Available: " + inventory.getQuantity() + ", Requested: " + quantity);
		}

		// Step 3: If this is a non-consumable, also check the count of available physical items.
		if (!"Consumables".equalsIgnoreCase(categoryName)) {
			long availableItemCount = itemRepository.countByItemNameAndStatus(itemName, "Available");
			if(availableItemCount < quantity) {
				throw new IllegalStateException("Not enough individual items available for " + itemName + ". Available units: " + availableItemCount + ", Requested: " + quantity);
			}
		}

		// Step 4: Reduce the stock from the main inventory because it's now 'reserved' for preparation.
		inventory.setQuantity(inventory.getQuantity() - quantity);
		invrepo.save(inventory);

		// Step 5: Create the new PreparingItemEntity record.
		PreparingItemEntity preparingItem = new PreparingItemEntity();
		preparingItem.setItemName(itemName);
		preparingItem.setCategoryName(categoryName);
		preparingItem.setQuantity(quantity);
		preparingItem.setStatus(status);
		preparingItem.setInstiId(instiId);

		UserEntity user = userRepository.findByInstiId(instiId);
		if (user != null) {
			preparingItem.setUser(user);
		} else {
			logger.warn("User not found for instiId: {}", instiId);
		}

		// Step 6: *** This is the crucial link. ***
		// Associate the preparing item with its parent inventory record.
		preparingItem.setInventory(inventory);

		// Step 7: Save the new preparing item, now with the correct link to inventory.
		return preparingItemRepository.save(preparingItem);
	}

	public List<PreparingItemEntity> getPreparingItems(String instiId, String status) {
		if (instiId == null) {
			return preparingItemRepository.findByStatus(status);
		}
		return preparingItemRepository.findByInstiIdAndStatus(instiId, status);
	}

	@Transactional
	public void proceedToCheckOut(Map<Integer, Integer> itemQuantities, Map<Integer, Map<String, String>> uniqueIdsMap) {
		for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
			Integer prepItemId = entry.getKey();
			int quantity = entry.getValue();
			PreparingItemEntity prepItem = preparingItemRepository.findById(prepItemId).orElseThrow();
			UserEntity user = prepItem.getUser();
			String categoryName = prepItem.getCategoryName();
			String itemName = prepItem.getItemName();
			prepItem.setStatus("In-use");
			// No need to save here, @Transactional handles it.

			if (categoryName == null || !categoryName.equalsIgnoreCase("Consumables")) {
				Map<String, String> uniqueIdMap = uniqueIdsMap.get(prepItemId);
				int handled = 0;
				if (uniqueIdMap != null) {
					for (String uid : uniqueIdMap.values()) {
						if (uid != null && !uid.isEmpty()) {
							ItemEntity item = itemRepository.findByUniqueIdAndIsDeletedFalse(uid);
							if (item != null && "Available".equals(item.getStatus())) {
								item.setStatus("In-use");
								item.setUser(user);
								item.setPreparingItem(prepItem);
								handled++;
							}
						}
					}
				}

				int remaining = quantity - handled;
				if (remaining > 0) {
					List<ItemEntity> autoItems = itemRepository
							.findByItemNameAndIsAutoUidTrueAndStatusAndIsDeletedFalse(itemName, "Available", PageRequest.of(0, remaining));
					for (ItemEntity item : autoItems) {
						item.setStatus("In-use");
						item.setUser(user);
						item.setPreparingItem(prepItem);
					}

					int fallback = remaining - autoItems.size();
					if (fallback > 0) {
						List<ItemEntity> manualItems = itemRepository
								.findByItemNameAndIsAutoUidFalseAndStatusAndIsDeletedFalse(itemName, "Available", PageRequest.of(0, fallback));
						for (ItemEntity item : manualItems) {
							item.setStatus("In-use");
							item.setUser(user);
							item.setPreparingItem(prepItem);
						}
					}
				}
			} else {
				// For consumables, the inventory quantity was already reduced in addToPreparingItem.
				// Nothing more to do here for checkout.
			}
		}
	}

	public Map<String, Object> getTeacherScheduleByPreparingItemId(int preparingItemId) {
		PreparingItemEntity prep = preparingItemRepository.findById(preparingItemId)
				.orElseThrow(() -> new RuntimeException("Preparing item not found with ID: " + preparingItemId));
		TeacherScheduleEntity ts = prep.getTeacherSchedule();
		if (ts != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("date", ts.getDate());
			map.put("startTime", ts.getStartTime() != null ? ts.getStartTime().format(TIME_FORMATTER) : null);
			map.put("endTime", ts.getEndTime() != null ? ts.getEndTime().format(TIME_FORMATTER) : null);
			map.put("labNum", ts.getLabNum());
			return map;
		}
		return null;
	}

	/**
	 * UPDATED METHOD
	 * Cancels a batch of prepared items. It now safely finds the linked inventory
	 * and returns the reserved quantity to the main stock.
	 */
	@Transactional
	public void cancelPreparingBatch(String referenceCode) {
		List<PreparingItemEntity> batch = preparingItemRepository.findByReferenceCode(referenceCode);
		if (batch.isEmpty()) {
			throw new NoSuchElementException("No such batch: " + referenceCode);
		}

		for (PreparingItemEntity prep : batch) {
			// Mark as canceled
			prep.setStatus("Canceled");

			InventoryEntity inv = prep.getInventory();

			if (inv != null) {
				// Normal flow if inventory is linked
				inv.setQuantity(inv.getQuantity() + prep.getQuantity());
				logger.info("Restocked {} of {} to inventory.", prep.getQuantity(), prep.getItemName());
			} else {
				// Recovery for old data: attempt to find inventory by name
				InventoryEntity fallbackInv = invrepo.findByName(prep.getItemName())
						.orElseThrow(() -> new NoSuchElementException("Inventory not found for item: " + prep.getItemName()));

				// Restock and link the inventory back
				fallbackInv.setQuantity(fallbackInv.getQuantity() + prep.getQuantity());
				prep.setInventory(fallbackInv); // Optional: re-link for data integrity

				logger.warn("Recovered missing inventory link for PreparingItem ID: {}. Restocked {} of {}.",
						prep.getId(), prep.getQuantity(), prep.getItemName());
			}
		}
		// No need for explicit save due to @Transactional
	}



	@Transactional
	public void manualRestockByPreparingItemId(int preparingItemId) {
		logger.info("Attempting manual restock for PreparingItem ID: {}", preparingItemId);

		// 1. Find the specific preparing_item row
		PreparingItemEntity prepItem = preparingItemRepository.findById(preparingItemId)
				.orElseThrow(() -> new NoSuchElementException("No PreparingItem found with ID: " + preparingItemId));

		// 2. Safety Check: Only run for canceled items that have a missing inventory link
		if (!"Canceled".equalsIgnoreCase(prepItem.getStatus())) {
			throw new IllegalStateException("Manual restock is only for items with 'Canceled' status.");
		}
		if (prepItem.getInventory() != null) {
			throw new IllegalStateException("This item already has an inventory link. No action taken.");
		}

		String itemName = prepItem.getItemName();
		if (itemName == null || itemName.isEmpty()) {
			throw new IllegalStateException("PreparingItem ID: " + preparingItemId + " has no item name.");
		}

		// 3. Find the parent inventory using the item's NAME
		InventoryEntity inventory = invrepo.findByName(itemName)
				.orElseThrow(() -> new NoSuchElementException("Manual restock failed: Inventory not found for item name: " + itemName));

		// 4. Add the quantity back to the inventory
		int quantityToRestock = prepItem.getQuantity();
		inventory.setQuantity(inventory.getQuantity() + quantityToRestock);

		logger.info("Manual restock successful! Returned {} of '{}' to inventory. New quantity: {}",
				quantityToRestock, itemName, inventory.getQuantity());

		// The @Transactional annotation will save the change to the inventory.
	}

}