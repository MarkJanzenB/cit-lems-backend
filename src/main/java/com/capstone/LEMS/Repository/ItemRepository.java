package com.capstone.LEMS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.ItemEntity;

@Repository
public interface ItemRepository  extends JpaRepository<ItemEntity, Integer>{
	ItemEntity findByUniqueId (String uniqueId);
	ItemEntity findTopByIsAutoUidTrueOrderByItemIdDesc();
	List<ItemEntity> findByItemName(String itemName);
	List<ItemEntity> findByItemNameAndUserIsNullOrderByItemIdDesc(String itemName);
	List<ItemEntity> findByItemNameAndStatus(String itemName, String status);
	List<ItemEntity> findByItemNameAndBorrowCart_Id(String itemName, int borrowCartId);
	List<ItemEntity> findByUniqueIdIn(List<String> uniqueIds);
	List<ItemEntity> findByBatchResupply(BatchResupplyEntity batchResupply);
}
