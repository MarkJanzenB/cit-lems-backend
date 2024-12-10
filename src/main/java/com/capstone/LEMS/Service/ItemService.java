package com.capstone.LEMS.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Repository.ItemRepository;

@Service
public class ItemService {
	private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    @Autowired
    ItemRepository itemrepo;
    
    @Autowired
    IdCounterService idcountserv;

    @Transactional
    public ResponseEntity<?> AddItem(ItemEntity item, int bulkSize) {
    	if(bulkSize <= 0) {
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST) // 400
    				.body("Invalid bulk size. It must be greater than zero");
    	}
    	
    	ItemEntity itemfromdb = itemrepo.findByUniqueId(item.getUniqueId());
    	
    	if(itemfromdb != null) {
    		return ResponseEntity
    				.status(HttpStatus.CONFLICT) // 409
    				.body("Item with "+item.getUniqueId()+" unique ID already exists");
    	}
    	
    	List<ItemEntity> itemsToSave = new ArrayList<>();
    	
    	for(int i = 1; i <= bulkSize; i++) {
    		ItemEntity newItem = new ItemEntity();
    		
    		newItem.setItemName(item.getItemName());
    		newItem.setGroup(item.getGroup());
    		newItem.setInventory(item.getInventory());
    		newItem.setUniqueId(item.getUniqueId());
    		
    		if(newItem.getUniqueId() == null || item.getUniqueId().isBlank()) {
        		String prefix = item.getItemName().substring(0, 2).toUpperCase()
        				+ item.getItemName().substring(item.getItemName().length() - 1).toUpperCase();
        		
        		int nextNumber = idcountserv.getNextId() + i;
        		String uniqueId = prefix + String.format("%04d", nextNumber);
        		newItem.setUniqueId(uniqueId);
        		newItem.setAutoUid(true);
        	}
    		
    		itemsToSave.add(newItem);
    	}
    	
    	List<ItemEntity> savedItems = itemrepo.saveAll(itemsToSave);
    	
        return ResponseEntity
        		.status(HttpStatus.CREATED) // 201
        		.body(savedItems);
    }
    
    public ResponseEntity<?> updateItems(String itemToEdit, ItemEntity newItemDetails){
    	log.info("Starting updateItems for itemToEdit: {}", itemToEdit);
    	
    	// Important learnings: trim() removes whitespace on both sides of a string
    	if(newItemDetails.getItemName() == null || newItemDetails.getItemName().trim().isEmpty()) {
    		log.warn("Validation failed: newItemDetails.getItemName() is blank or null");
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST) // 400
    				.body("Blank item name is not allowed.");
    	}
    	
    	log.info("Fetching items with name: {}", itemToEdit);
    	List<ItemEntity> items = itemrepo.findByItemName(itemToEdit);
    	
    	if(items.isEmpty()) {
    		log.warn("No items found with name: {}", itemToEdit);
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND) // 404
    				.body("No items found with name: " + itemToEdit);
    	}
    	
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
    
    public ResponseEntity<?> deleteItems(int bulkSize, ItemEntity itemsToDelete){
    	if(itemsToDelete.getItemName() == null || itemsToDelete.getItemName().trim().isEmpty()) {
    		log.warn("Validation failed: itemsToDelete.getItemName() is blank or null");
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST) // 400
    				.body("Blank item name is not allowed.");
    	}
    	
    	log.info("Fetching items with name: {}", itemsToDelete.getItemName());
    	List<ItemEntity> items = itemrepo.findByItemNameAndGroupIsNullOrderByItemIdDesc(itemsToDelete.getItemName());
    	
    	if(items.isEmpty()) {
    		log.warn("No items found with name: {}", itemsToDelete.getItemName());
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND) // 404
    				.body("No items with name: " + itemsToDelete.getItemName());
    	}
    	
    	if(items.size() < bulkSize) {
    		log.warn("Attempting to delete more items than available or attempting to delete items that are used by a group");
    		return ResponseEntity
    				.status(HttpStatus.BAD_REQUEST) // 400
    				.body("Cannot delete items more than the available or that some items are currently in used by a group");
    	}
    	
    	List<ItemEntity> itemsToDeleteList = items.subList(0, bulkSize);
    	
    	log.info("Deleting {} items with name: {}", itemsToDeleteList.size(), itemsToDelete.getItemName());
    	itemrepo.deleteAll(itemsToDeleteList);
    	
    	log.info("Successfully deleted {} items", itemsToDeleteList.size());
    	return ResponseEntity
    			.status(HttpStatus.OK) // 200
    			.body("Successfully deleted " + itemsToDeleteList.size() + " items.");
    }

    public List<ItemEntity> getAllItems(){
        return itemrepo.findAll();
    }
}
