package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ItemCategoryEntity;
import com.capstone.LEMS.Repository.ItemCategoryRepository;

@Service
public class ItemCategoryService {
    @Autowired
    ItemCategoryRepository itemCatRepo;

    public ItemCategoryEntity addItemCategory(ItemCategoryEntity itemCategory) {
        return itemCatRepo.save(itemCategory);
    }

    public List<ItemCategoryEntity> getAllItemCategories() {
        return itemCatRepo.findAll();
    }
}
