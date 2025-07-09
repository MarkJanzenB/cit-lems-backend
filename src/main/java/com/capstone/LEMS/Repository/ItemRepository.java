package com.capstone.LEMS.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.capstone.LEMS.Entity.PreparingItemEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.BorrowCartEntity;


@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
	// Updated methods to include isDeleted = false (for general active item retrieval)
	ItemEntity findByUniqueIdAndIsDeletedFalse(String uniqueId);
	ItemEntity findTopByIsAutoUidTrueAndIsDeletedFalseOrderByItemIdDesc();
	List<ItemEntity> findByItemNameAndIsDeletedFalse(String itemName);
	List<ItemEntity> findByItemNameAndUserIsNullAndIsDeletedFalseOrderByItemIdDesc(String itemName);
	List<ItemEntity> findByItemNameAndStatusAndIsDeletedFalse(String itemName, String status);
	List<ItemEntity> findByItemNameAndBorrowCart_IdAndIsDeletedFalse(String itemName, int borrowCartId);
	List<ItemEntity> findByUniqueIdInAndIsDeletedFalse(List<String> uniqueIds);

	// NEW: Method to find items by BatchResupply without filtering by isDeleted status
	List<ItemEntity> findByBatchResupply(BatchResupplyEntity batchResupply);

	// Existing: Method to find items by BatchResupply AND isDeleted=false (for active items)
	List<ItemEntity> findByBatchResupplyAndIsDeletedFalse(BatchResupplyEntity batchResupply);

	List<ItemEntity> findByItemNameAndIsAutoUidTrueAndStatusAndIsDeletedFalse(String itemName, String status, Pageable pageable);
	List<ItemEntity> findByItemNameAndIsAutoUidFalseAndStatusAndIsDeletedFalse(String itemName, String status, Pageable pageable);
	List<ItemEntity> findByPreparingItem_IdInAndIsDeletedFalse(List<Integer> preparingItemIds);
	List<ItemEntity> findByItemIdAndIsDeletedFalse(int itemId);
	ItemEntity findByItemNameIgnoreCaseAndIsDeletedFalse(String itemName);

	// Instead of findAll, provide a method to get all non-deleted items
	List<ItemEntity> findByIsDeletedFalse();

	long countByItemNameAndStatus(String itemName, String status);

	List<ItemEntity> findByPreparingItem(PreparingItemEntity prep);


	// Existing custom queries updated to include i.isDeleted = false
	@Query("SELECT DISTINCT i.variant FROM ItemEntity i WHERE i.itemName = :itemName AND i.status = :status AND i.variant IS NOT NULL AND i.variant <> '' AND i.isDeleted = false")
	List<String> findVariantsByItemNameAndStatus(@Param("itemName") String itemName, @Param("status") String status);

	@Query("SELECT DISTINCT i.variant FROM ItemEntity i WHERE i.inventory.inventoryId = :inventoryId AND i.variant IS NOT NULL AND i.variant <> '' AND i.isDeleted = false")
	List<String> findVariantsByInventoryId(@Param("inventoryId") Integer inventoryId);


	// 1. Method to find an existing consumable batch by inventory, variant, and expiry date
	Optional<ItemEntity> findByInventory_InventoryIdAndVariantAndExpiryDateAndIsDeletedFalse(
			Integer inventoryId, String variant, LocalDate expiryDate);

	// 2. Method to sum total quantity for a given Inventory (for updating InventoryEntity)
	// This sum should only include non-deleted items for accurate current stock count
	@Query("SELECT COALESCE(SUM(i.quantity), 0) FROM ItemEntity i WHERE i.inventory.inventoryId = :inventoryId AND i.isDeleted = false")
	Integer sumQuantityByInventoryId(@Param("inventoryId") Integer inventoryId);


	// For consumables: returns variant name and its quantity from the batch ItemEntity
	@Query("SELECT i.variant, i.quantity FROM ItemEntity i WHERE i.itemName = :itemName AND i.status = :status AND i.inventory.itemCategory.categoryName = 'Consumables' AND i.isDeleted = false")
	List<Object[]> findConsumableVariantQuantities(@Param("itemName") String itemName, @Param("status") String status);


	long countByInventory_InventoryIdAndStatusAndIsDeletedFalse(
			Integer inventoryId,
			String status
	);
}

