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

    public List<BorrowItem> getBorrowItemsByInstiId(String instiId) {
        List<BorrowItem> items = borrowItemRepository.findByInstiId(instiId);
        System.out.println("Fetched items for instiId " + instiId + ": " + items);
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


    public void saveBorrowItem(BorrowItem borrowItem) {
        borrowItemRepository.save(borrowItem);
    }


}