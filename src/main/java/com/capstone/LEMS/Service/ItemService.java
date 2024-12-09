package com.capstone.LEMS.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Repository.ItemRepository;

@Service
public class ItemService {
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

    public List<ItemEntity> getAllItems(){
        return itemrepo.findAll();
    }
}
