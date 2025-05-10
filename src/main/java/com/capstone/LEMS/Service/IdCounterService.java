package com.capstone.LEMS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Repository.ItemRepository;

@Service
public class IdCounterService {
    
    @Autowired
    private ItemRepository itemRepo;
    
    public int getNextId() {
        ItemEntity latestItem = itemRepo.findTopByIsAutoUidTrueOrderByItemIdDesc();
        
        if (latestItem != null && latestItem.getUniqueId() != null) {
            String uniqueId = latestItem.getUniqueId();
            String numberPart = uniqueId.replaceAll("\\D", "");
            return Integer.parseInt(numberPart) + 1;
        }
        
        return 1;
    }
}
