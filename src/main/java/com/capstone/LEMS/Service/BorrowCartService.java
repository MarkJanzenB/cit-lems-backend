// File: src/main/java/com/capstone/LEMS/Service/BorrowCartService.java
package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import com.capstone.LEMS.Entity.PreparingItemEntity;
import jakarta.transaction.Transactional;
import com.capstone.LEMS.Repository.BorrowCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class BorrowCartService {

    @Autowired
    private BorrowCartRepository borrowCartRepository;

    private static final Logger log = LoggerFactory.getLogger(BorrowCartService.class);

    public List<BorrowCartEntity> getAllBorrowCarts() {
        return borrowCartRepository.findAll();
    }

    public BorrowCartEntity addToBorrowCart(String instiId, int itemId, String itemName, String categoryName, int quantity) {
        BorrowCartEntity existingBorrowCart = borrowCartRepository.findByItemIdAndInstiIdStrict(itemId, instiId);

        if (existingBorrowCart != null) {
            log.info("Existing borrow cart: {} ", existingBorrowCart.getInstiId());
            existingBorrowCart.setQuantity(existingBorrowCart.getQuantity() + quantity);
            return borrowCartRepository.save(existingBorrowCart);
        }

        // Add the item to the borrow cart WITHOUT modifying inventory
        BorrowCartEntity borrowCart = new BorrowCartEntity(instiId, itemId, itemName, categoryName, quantity);
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
        List<BorrowCartEntity> cartItems = borrowCartRepository.findByInstiId(instiId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No items in the borrow cart to finalize.");
        }

        for (BorrowCartEntity cartItem : cartItems) {
            PreparingItemEntity preparingItem = new PreparingItemEntity();
            preparingItem.setInstiId(instiId);
            preparingItem.setItemId(cartItem.getItemId());
            preparingItem.setItemName(cartItem.getItemName());
            preparingItem.setCategoryName(cartItem.getCategoryName());
            preparingItem.setQuantity(cartItem.getQuantity());
            preparingItem.setStatus("Preparing"); // Set status as "Preparing"

            // Save to preparing_item table
            preparingItemRepository.save(preparingItem);
        }

        // Clear the borrow cart after moving items to preparing_item
        borrowCartRepository.deleteByInstiId(instiId);
    }

}
