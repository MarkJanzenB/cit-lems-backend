// File: src/main/java/com/capstone/LEMS/Service/BorrowCartService.java
package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.InventoryRepository;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Repository.UserRepository;

import jakarta.transaction.Transactional;
import com.capstone.LEMS.Repository.BorrowCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BorrowCartService {

    @Autowired
    private BorrowCartRepository borrowCartRepository;

    @Autowired
    private PreparingItemRepository preparingItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private PreparingItemService preparingItemService; // Autowire PreparingItemService
    
    @Autowired
    UserRepository userrepo;


    private static final Logger log = LoggerFactory.getLogger(BorrowCartService.class);

    public List<BorrowCartEntity> getAllBorrowCarts() {
        return borrowCartRepository.findAll();
    }

    public BorrowCartEntity addToBorrowCart(String instiId, String itemName, String categoryName, int quantity) {
        // Find existing borrow cart based on itemName and instiId
        BorrowCartEntity existingBorrowCart = borrowCartRepository.findByItemNameAndInstiId(itemName, instiId);

        if (existingBorrowCart != null) {
            log.info("Existing borrow cart: {} ", existingBorrowCart.getInstiId());
            // If item already exists in the borrow cart, just update the quantity
            existingBorrowCart.setQuantity(existingBorrowCart.getQuantity() + quantity);
            return borrowCartRepository.save(existingBorrowCart);
        }

        // Otherwise, create a new borrow cart entity with item details and save
        BorrowCartEntity borrowCart = new BorrowCartEntity(instiId, itemName, categoryName, quantity);
        return borrowCartRepository.save(borrowCart);
    }

    public List<BorrowCartEntity> getBorrowCartsByInsti(String instiId) {
        return borrowCartRepository.findByInstiId(instiId);
    }

    @Transactional
    public void clearCart(String instiId) {
        log.info("Clearing borrow cart for instiId: " + instiId);
        borrowCartRepository.deleteByInstiId(instiId);
    }

    public void deleteBorrowCart(int id) {
        BorrowCartEntity borrowCart = borrowCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow cart item not found"));

        // Remove item from the borrow cart WITHOUT modifying inventory
        log.info("Removing item from borrow cart: " + borrowCart.getItemName());
        borrowCartRepository.delete(borrowCart);
    }



    @Transactional
    public void moveToPreparingItem(String instiId) {
        log.info("Starting move to preparing items for instiId: {}", instiId);
        System.out.println("Fetching borrow cart for instiId: " + instiId);

        List<BorrowCartEntity> carts = borrowCartRepository.findByInstiId(instiId);
        if (carts.isEmpty()) {
            log.warn("No items to move for instiId: {}", instiId);
            throw new IllegalStateException("No items to move for instiId: " + instiId);
        }

        // Generate a single reference ID for all items from the borrow cart.
        String referenceId = "PI-" + instiId + "-" + ThreadLocalRandom.current().nextInt(1000, 10000);

        for (BorrowCartEntity cartItem : carts) {
            log.info("Processing item: {} for instiId: {}", cartItem.getItemName(), instiId);
            System.out.println("Processing item: " + cartItem.getItemName() + " for instiId: " + instiId);
            
            UserEntity user = userrepo.findByInstiId(instiId);

            PreparingItemEntity preparingItem = new PreparingItemEntity();
            preparingItem.setInstiId(instiId);
            preparingItem.setItemName(cartItem.getItemName());
            preparingItem.setCategoryName(cartItem.getCategoryName());
            preparingItem.setQuantity(cartItem.getQuantity());
            preparingItem.setStatus("Preparing");
            preparingItem.setUser(user);
            preparingItem.setDateCreated(LocalDate.now());

            InventoryEntity inventoryItem = inventoryRepository.findByNameIgnoreCase(cartItem.getItemName());
            if (inventoryItem == null) {
                log.error("Item not found in inventory: {}", cartItem.getItemName());
                throw new RuntimeException("Item not found in inventory: " + cartItem.getItemName());
            }

            log.info("Inventory item: {}, Quantity: {}, Requested: {}", inventoryItem.getName(), inventoryItem.getQuantity(), cartItem.getQuantity());

            if (inventoryItem.getQuantity() < cartItem.getQuantity()) {
                log.error("Not enough stock for item: {}. Requested: {}, Available: {}", cartItem.getItemName(), cartItem.getQuantity(), inventoryItem.getQuantity());
                throw new RuntimeException("Not enough stock available in inventory for item: " + cartItem.getItemName() + ". Requested: " + cartItem.getQuantity() + ", Available: " + inventoryItem.getQuantity());
            }

            inventoryItem.setQuantity(inventoryItem.getQuantity() - cartItem.getQuantity());
            inventoryRepository.save(inventoryItem);
            log.info("Inventory updated for item: {}, New Quantity: {}", inventoryItem.getName(), inventoryItem.getQuantity());

            // Set the generated reference ID.
            preparingItem.setReferenceCode(referenceId);

            preparingItemRepository.save(preparingItem);
            log.info("Preparing item created with reference ID: {}", referenceId);

            // Do NOT call finalizePreparingItem here.
        }

        borrowCartRepository.deleteByInstiId(instiId);
        log.info("Borrow cart cleared for instiId: {}", instiId);
        log.info("Finished move to preparing items for instiId: {}", instiId);
    }
}