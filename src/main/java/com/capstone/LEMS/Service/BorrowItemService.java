



package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Repository.BorrowItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowItemService {

    @Autowired
    private BorrowItemRepository borrowItemRepository;

    public void addBorrowItems(List<BorrowItem> borrowItems) {
        borrowItemRepository.saveAll(borrowItems);
    }

    public List<BorrowItem> getAllBorrowItems() {
        return borrowItemRepository.findAll();
    }
}


//package com.capstone.LEMS.Service;
//
//import com.capstone.LEMS.Entity.BorrowItem;
//import com.capstone.LEMS.Repository.BorrowItemRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//
//@Service
//public class BorrowItemService {
//
//    @Autowired
//    private BorrowItemRepository borrowItemRepository;
//
//    // BorrowItemService.java
//    public ResponseEntity<?> addBorrowItems(List<BorrowItem> borrowItems) {
//        return ResponseEntity.ok(borrowItemRepository.saveAll(borrowItems));
//    }
//
//
//    public BorrowItem updateBorrowItem(Long id, int instiId, int itemId, String itemName, String categoryName, int quantity, String borrower) {
//        BorrowItem borrowItem = borrowItemRepository.findById(id).orElseThrow(() -> new RuntimeException("BorrowItem not found"));
//        borrowItem.setInstiId(instiId);
//        borrowItem.setItemId(itemId);
//        borrowItem.setItemName(itemName);
//        borrowItem.setCategoryName(categoryName);
//        borrowItem.setQuantity(quantity);
//        borrowItem.setBorrower(borrower);
//        return borrowItemRepository.save(borrowItem);
//    }
//
//    public List<BorrowItem> getAllBorrowItems() {
//        return borrowItemRepository.findAll();
//    }
//
//    public List<BorrowItem> getBorrowItemsByInsti(Long instiId) {
//        return borrowItemRepository.findByInstiId(instiId);
//    }
//
//
//
////    public ResponseEntity<List<BorrowItem>> getBorrowItemsByBorrower(String borrower) {
////        List<BorrowItem> borrowItems = borrowItemRepository.findByBorrower(borrower);
////        return ResponseEntity.ok(borrowItems);
////    }
//
////    public void deleteBorrowItem(List<Integer> id) {
////       return borrowItemRepository.deleteAllById(id);
////    }
//
//
//
//}