package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Repository.ItemRepository;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemrepo;

    //for adding items
    public ItemEntity AddItem(ItemEntity item) {
    	ItemEntity itemfromdb = itemrepo.findByUniqueId(item.getUniqueId());
    	
    	if(itemfromdb != null) {
    		throw new IllegalArgumentException("Item already exists");
    	}
        return itemrepo.save(item);
    }

    //get a list of all items
    public List<ItemEntity> getAllItems(){
        return itemrepo.findAll();
    }
}
