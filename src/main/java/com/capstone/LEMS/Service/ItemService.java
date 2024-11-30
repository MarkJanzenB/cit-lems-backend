package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Repository.ItemRepository;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemrepo;

    //for adding items
    public ResponseEntity<?> AddItem(ItemEntity item) {
    	ItemEntity itemfromdb = itemrepo.findByUniqueId(item.getUniqueId());
    	
    	if(itemfromdb != null) {
    		return ResponseEntity
    				.status(HttpStatus.CONFLICT) // 409
    				.body("Item with "+item.getUniqueId()+" unique ID already exists");
    	}
        return ResponseEntity
        		.status(HttpStatus.CREATED) // 201
        		.body(itemrepo.save(item));
    }

    //get a list of all items
    public List<ItemEntity> getAllItems(){
        return itemrepo.findAll();
    }
}
