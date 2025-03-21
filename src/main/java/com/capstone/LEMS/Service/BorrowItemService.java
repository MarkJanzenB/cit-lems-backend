package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BorrowCart;
import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Repository.BorrowItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowItemService {

    @Autowired
    private BorrowItemRepository borrowItemRepository;

    public List<BorrowItem> getBorrowItemsByUid(int uid) {
        List<BorrowItem> items = borrowItemRepository.findByUser_UserId(uid);
        System.out.println("Fetched items for uid " + uid + ": " + items);
        return items != null ? items : List.of();
    }




    public ResponseEntity<?> addBorrowItem(BorrowItem borrowItem) {
        try {
            BorrowItem savedItem = borrowItemRepository.save(borrowItem);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add borrow item.");
        }
    }

    public List<BorrowItem> getAllBorrowItems() {
        return borrowItemRepository.findAll(); // Fetch all borrow items
    }

    public List<BorrowItem> getAllItems() {
        return borrowItemRepository.findAll(); // Fetch all borrow items from DB
    }


    public void saveBorrowItem(BorrowItem borrowItem) {
        borrowItemRepository.save(borrowItem);
    }


    @Transactional
    public boolean updateBorrowItemStatus(String borrowedId, String status) {
        List<BorrowItem> items = borrowItemRepository.findByBorrowedId(borrowedId);
        if (items.isEmpty()) {
            return false; // Borrowed ID not found
        }

        for (BorrowItem item : items) {
            item.setStatus(status);
        }
        borrowItemRepository.saveAll(items); // Save updated statuses in batch
        return true;
    }


    public void updateStatusByBorrowedId(String borrowedId, String status) {
        List<BorrowItem> borrowItems = borrowItemRepository.findByBorrowedId(borrowedId);
        for (BorrowItem item : borrowItems) {
            item.setStatus(status);
            borrowItemRepository.save(item);
        }
    }


}