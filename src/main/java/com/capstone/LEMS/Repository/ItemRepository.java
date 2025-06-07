package com.capstone.LEMS.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.BorrowCartEntity; // Make sure this is imported if used in findByItemNameAndVariantAndBorrowCart
												  // PS: this import is never used


@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
	ItemEntity findByUniqueId(String uniqueId);
	ItemEntity findTopByIsAutoUidTrueOrderByItemIdDesc();
	List<ItemEntity> findByItemName(String itemName);
	List<ItemEntity> findByItemNameAndUserIsNullOrderByItemIdDesc(String itemName);
	List<ItemEntity> findByItemNameAndStatus(String itemName, String status);
	List<ItemEntity> findByItemNameAndBorrowCart_Id(String itemName, int borrowCartId);
	List<ItemEntity> findByUniqueIdIn(List<String> uniqueIds);
	List<ItemEntity> findByBatchResupply(BatchResupplyEntity batchResupply);
	List<ItemEntity> findByItemNameAndIsAutoUidTrueAndStatus(String itemName, String status, Pageable pageable);
	List<ItemEntity> findByItemNameAndIsAutoUidFalseAndStatus(String itemName, String status, Pageable pageable);
	List<ItemEntity> findByPreparingItem_IdIn(List<Integer> preparingItemIds);
	List<ItemEntity> findByItemId(int itemId);
	ItemEntity findByItemNameIgnoreCase(String itemName);


	// AI slop
	// also try to not to use query annotations as much as possible next time (Rule #4)
	/*
	 * TODO: Baclayon
	 * -Remove these obvious AI comments
	 * -If applicable: Do not use query annotations
	 * */
	// Existing custom queries you provided:
	@Query("SELECT DISTINCT i.variant FROM ItemEntity i WHERE i.itemName = :itemName AND i.status = :status AND i.variant IS NOT NULL AND i.variant <> ''")
	List<String> findVariantsByItemNameAndStatus(@Param("itemName") String itemName, @Param("status") String status);

	@Query("SELECT DISTINCT i.variant FROM ItemEntity i WHERE i.inventory.inventoryId = :inventoryId AND i.variant IS NOT NULL AND i.variant <> ''")
	List<String> findVariantsByInventoryId(@Param("inventoryId") Integer inventoryId);


	// 1. Method to find an existing consumable batch by inventory, variant, and expiry date
	Optional<ItemEntity> findByInventory_InventoryIdAndVariantAndExpiryDate(
			Integer inventoryId, String variant, LocalDate expiryDate);

	// 2. Method to sum total quantity for a given Inventory (for updating InventoryEntity)
	@Query("SELECT COALESCE(SUM(i.quantity), 0) FROM ItemEntity i WHERE i.inventory.inventoryId = :inventoryId")
	Integer sumQuantityByInventoryId(@Param("inventoryId") Integer inventoryId);


	// For consumables: returns variant name and its quantity from the batch ItemEntity
	@Query("SELECT i.variant, i.quantity FROM ItemEntity i WHERE i.itemName = :itemName AND i.status = :status AND i.inventory.itemCategory.categoryName = 'Consumables'")
	List<Object[]> findConsumableVariantQuantities(@Param("itemName") String itemName, @Param("status") String status);



}