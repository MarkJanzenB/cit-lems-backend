package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BorrowCart;
import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Repository.BorrowCartRepository;
import com.capstone.LEMS.Repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class BorrowCartService {

    @Autowired
    private BorrowCartRepository borrowCartRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    private static final Logger log = LoggerFactory.getLogger(BorrowCartService.class);

    public List<BorrowCart> getAllBorrowCarts() {
        return borrowCartRepository.findAll();
    }

    public BorrowCart addToBorrowCart(String instiId, int itemId, String itemName, String categoryName, int quantity) {

        BorrowCart existingBorrowCart = borrowCartRepository.findByItemIdAndInstiIdStrict(itemId, instiId);

        if(existingBorrowCart != null) {
            log.info("Existing borrow cart: {} " , existingBorrowCart.getInstiId());
            existingBorrowCart.setQuantity(existingBorrowCart.getQuantity() + quantity);

            // Deduct the item quantity from the inventory
            InventoryEntity inventoryItem = inventoryRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

            if (inventoryItem.getQuantity() < quantity) {
                throw new RuntimeException("Not enough items in inventory");
            }

            inventoryItem.setQuantity(inventoryItem.getQuantity() - quantity);
            inventoryRepository.save(inventoryItem);

            return borrowCartRepository.save(existingBorrowCart);
        }

        // Deduct the item quantity from the inventory
        InventoryEntity inventoryItem = inventoryRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        if (inventoryItem.getQuantity() < quantity) {
            throw new RuntimeException("Not enough items in inventory");
        }
        inventoryItem.setQuantity(inventoryItem.getQuantity() - quantity);
        inventoryRepository.save(inventoryItem);

        // Add the item to the borrow cart
        BorrowCart borrowCart = new BorrowCart(instiId, itemId, itemName, categoryName, quantity);
        return borrowCartRepository.save(borrowCart);
    }

    public List<BorrowCart> getBorrowCartsByInsti(String instiId) {
        return borrowCartRepository.findByInstiId(instiId);
    }

    public void deleteBorrowCart(Long id) {
        borrowCartRepository.deleteById(id);
    }
}