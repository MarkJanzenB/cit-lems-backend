package com.capstone.LEMS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;
    public List<InventoryEntity> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public ResponseEntity<?> addInventory(InventoryEntity inventory) {
    	InventoryEntity inventoryfromdb = inventoryRepository.findByNameIgnoreCase(inventory.getName());
    	
    	if(inventoryfromdb != null) {
    		return ResponseEntity
    				.status(HttpStatus.CONFLICT) // 409
    				.body(inventory.getName() + " Inventory already exists");
    	}
    	
    	return ResponseEntity
    			.status(HttpStatus.CREATED) //201
    			.body(inventoryRepository.save(inventory));
    }
    
    public List<InventoryEntity> getInventoryByCategory(int category_id){
    	return inventoryRepository.findByItemCategoryCategoryId(category_id);
    }
    
    public ResponseEntity<?> isInventoryExists(String inventoryName){
    	InventoryEntity inventoryFromDb = inventoryRepository.findByNameIgnoreCase(inventoryName);
    	
    	if(inventoryFromDb != null) {
    		return ResponseEntity
    				.status(HttpStatus.OK) // 200
    				.body(inventoryName + " Exists in the database");
    	}
    	
    	return ResponseEntity
    			.status(HttpStatus.CONFLICT) // 409
    			.body(inventoryName + " Does not exists in the database");
    }
    
    public ResponseEntity<?> deleteInventory(int id){
    	Optional<InventoryEntity> inventory = inventoryRepository.findById(id);
    	if(inventory.isPresent()) {
    		inventoryRepository.deleteById(id);
    		return ResponseEntity
    				.status(HttpStatus.OK) // 200
    				.body(inventory);
    	}else {
    		return ResponseEntity
    				.status(HttpStatus.NOT_FOUND) // 404
    				.body(id + " Does not exists");
    	}
    }
}
