package com.capstone.LEMS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Repository.InventoryRepository;

import java.util.List;
@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;
    public List<InventoryEntity> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public InventoryEntity addInventory(InventoryEntity inventory) {
    	return inventoryRepository.save(inventory);
    }
    
    public List<InventoryEntity> getInventoryByCategory(int category_id){
    	return inventoryRepository.findByItemCategoryCategoryId(category_id);
    }
}
