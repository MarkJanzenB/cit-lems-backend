package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.UserRepository; // Make sure you import the UserRepository
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class PreparingItemService {

    @Autowired
    private PreparingItemRepository preparingItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository; // Add a UserRepository to fetch user details by instiId

    private static final Logger logger = LoggerFactory.getLogger(PreparingItemService.class); // Initialize logger




    public PreparingItemEntity addToPreparingItem(String instiId, String itemName, String categoryName, int quantity, String status) {
        PreparingItemEntity item = new PreparingItemEntity();
        item.setInstiId(instiId); // still good to save
        item.setItemName(itemName);
        item.setCategoryName(categoryName);
        item.setQuantity(quantity);
        item.setStatus(status);

        // ✅ Assign user based on instiId
        UserEntity user = userRepository.findByInstiId(instiId);
        if (user != null) {
            item.setUser(user);
        } else {
            logger.warn("User not found for instiId: {}", instiId);
        }

        return preparingItemRepository.save(item);
    }



    // Fetch preparing items along with the teacher's name based on instiId
    public List<Object[]> getPreparingItemsWithTeacherName(String instiId) {
        return preparingItemRepository.findPreparingItemsWithTeacherName(instiId);
    }

    // Fetch all preparing items by institution ID (removes duplicate method)
    public List<PreparingItemEntity> getPreparingItemsByInstiId(String instiId) {
        return preparingItemRepository.findByInstiId(instiId);
    }

    // Finalize preparing item and validate unique_id
    @Transactional
    public void finalizePreparingItem(int id, String uniqueId) {
        Optional<PreparingItemEntity> optionalItem = preparingItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            PreparingItemEntity preparingItem = optionalItem.get();

            // Set dateCreated here, outside the if (item != null) block
            logger.info("Setting dateCreated: {}", LocalDateTime.now());
            preparingItem.setDateCreated(LocalDateTime.now());

            ItemEntity item = preparingItem.getItem();

            if (item != null) {
                String itemUniqueId = item.getUniqueId();
                preparingItem.setUniqueId(itemUniqueId);
                preparingItem.setStatus("finalized");
                int referenceUniqueId = ThreadLocalRandom.current().nextInt(1000, 10000);
                preparingItem.setReferenceCode("PI-" + item.getItemName().charAt(0) + referenceUniqueId);
            } else {
                // Handle the case where item is null (if needed)
                // You might want to log a warning or throw an exception
                logger.warn("Item is null for preparingItem id: {}", id);
                preparingItem.setStatus("finalized"); // still set to finalized even if item is null.
                int referenceUniqueId = ThreadLocalRandom.current().nextInt(1000, 10000);
                preparingItem.setReferenceCode("PI-NullItem-" + referenceUniqueId);
            }

            preparingItemRepository.save(preparingItem);
        } else {
            throw new RuntimeException("Preparing item not found.");
        }
    }

    // Checkout preparing item and mark it as Borrowed
    public void checkoutPreparingItem(int id) {
        Optional<PreparingItemEntity> optionalItem = preparingItemRepository.findById(id);
        if (optionalItem.isPresent()) {
            PreparingItemEntity preparingItem = optionalItem.get();

            // Ensure the item is finalized before checking out
            if ("finalized".equals(preparingItem.getStatus())) {
                preparingItem.setStatus("Borrowed");  // Change status to Borrowed
                preparingItemRepository.save(preparingItem);  // Save the updated PreparingItem
            } else {
                throw new RuntimeException("Item is not finalized yet.");
            }
        } else {
            throw new RuntimeException("Preparing item not found.");
        }
    }

    public List<String> getAllUniqueItemIds() {
        return itemRepository.findAll().stream()
                .map(ItemEntity::getUniqueId)
                .distinct()
                .collect(Collectors.toList());
    }



    // Create a new preparing item
    public PreparingItemEntity createPreparingItem(PreparingItemEntity preparingItemEntity) {
        return preparingItemRepository.save(preparingItemEntity);
    }

    // Delete preparing item
    public void deletePreparingItem(int id) {
        preparingItemRepository.deleteById(id);
    }


    public List<PreparingItemEntity> getPreparingItemsByUid(String uid) {
        return preparingItemRepository.findByInstiId(uid);
    }

    public List<PreparingItemEntity> getAllPreparingItems() {
        return preparingItemRepository.findAll();
    }

}
