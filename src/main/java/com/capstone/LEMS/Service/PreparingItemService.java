package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Repository.InventoryRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.UserRepository; // Make sure you import the UserRepository
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class PreparingItemService {

    @Autowired
    private PreparingItemRepository preparingItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    InventoryRepository invrepo;

    /*
     * REFACTOR: remove loggers and sysouts once deployed
     * because this would cause too many logs in backend console
     * due to many request made by different users
     * */
    private static final Logger logger = LoggerFactory.getLogger(PreparingItemService.class); // Initialize logger
    
    public PreparingItemEntity addToPreparingItem(String instiId, String itemName, String categoryName, int quantity, String status) {
        PreparingItemEntity item = new PreparingItemEntity();
        item.setInstiId(instiId);
        item.setItemName(itemName);
        item.setCategoryName(categoryName);
        item.setQuantity(quantity);
        item.setStatus(status);

        UserEntity user = userRepository.findByInstiId(instiId);
        if (user != null) {
            item.setUser(user);
        } else {
            logger.warn("User not found for instiId: {}", instiId);
        }

        return preparingItemRepository.save(item);
    }

    /*
     * REFACTOR: either move the moveToPreparingItem logic in this service
     * for maintainablity
     * */

    public List<PreparingItemEntity> getPreparingItems(String instiId, String status) {
    	if(instiId == null) {
    		return preparingItemRepository.findByStatus(status);
    	}
    	return preparingItemRepository.findByInstiIdAndStatus(instiId, status);
    }
    
    @Transactional
    public void proceedToCheckOut(Map<Integer, Integer> itemQuantities, Map<Integer, Map<String, String>> uniqueIdsMap){

    	/*
    	 * Loop through all the PreparingItem record
    	 * based on its ID gathered from the key in itemQuantities
    	 * Then update their status
    	 * */
    	for(Map.Entry<Integer, Integer> entry: itemQuantities.entrySet()) {
    		Integer prepItemId = entry.getKey(); 
    		int quantity = entry.getValue(); 
    		PreparingItemEntity prepItem = preparingItemRepository.findById(prepItemId).orElseThrow();
    		UserEntity user = prepItem.getUser(); 
    		String categoryName = prepItem.getCategoryName();
    		String itemName = prepItem.getItemName();
    		prepItem.setStatus("In-use");
    		preparingItemRepository.save(prepItem);
			
    		if(categoryName == null || !categoryName.equalsIgnoreCase("Consumables")) {
    			/*
        		 * Finds the item base on unique id
        		 * and if the status is available
        		 * then update their status and user
        		 * */
        		Map<String, String> uniqueIdMap = uniqueIdsMap.get(prepItemId);
        		int handled = 0;
        		if(uniqueIdMap != null) {
        			for(String key: uniqueIdMap.keySet()) {
        				String uid = uniqueIdMap.get(key);
        				if (uid != null && !uid.isEmpty()) {
        					ItemEntity item = itemRepository.findByUniqueId(uid);
        					if(item != null && item.getStatus().equals("Available")) {
        						item.setStatus("In-use");
        						item.setUser(user);
        						itemRepository.save(item);
        						handled++;
        					}
        				}
        			}
        		}
        		
        		/*
        		 * Finds the item with a auto generated unique id
        		 * this will run if the number of quantity is greater than the
        		 * number of unique ids
        		 * */
        		int remaining = quantity - handled;
        		if(remaining > 0) {
        			List<ItemEntity> autoItems = itemRepository.findByItemNameAndIsAutoUidTrueAndStatus(
        					itemName, "Available", PageRequest.of(0, remaining));
        			
        			/*
        			 * Auto assign items
        			 * Prioritizing the items
        			 * with auto generated unique id
        			 * */
        			int autoHandled = 0;
        			for(ItemEntity item: autoItems) {
        				item.setStatus("In-use");
        				item.setUser(user);
        				itemRepository.save(item);
        				autoHandled++;
        			}
        			
        			/*
        			 * If items with auto generated unique id runs out
        			 * it will auto assign to the manual
        			 * */
        			int fallbackRemaining = remaining - autoHandled;
        			if(fallbackRemaining > 0) {
        				List<ItemEntity> manualItems = itemRepository.findByItemNameAndIsAutoUidFalseAndStatus(
        						itemName, "Available", PageRequest.of(0, fallbackRemaining));
        				for(ItemEntity item: manualItems) {
        					item.setStatus("In-use");
            				item.setUser(user);
            				itemRepository.save(item);
        				}
        			}
        		}
    		}
    	}
    }

}
