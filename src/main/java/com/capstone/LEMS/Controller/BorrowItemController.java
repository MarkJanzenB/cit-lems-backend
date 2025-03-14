package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Service.BorrowItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/api/borrowitem")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class BorrowItemController {

    @Autowired
    private BorrowItemService borrowItemService;

    @GetMapping("/insti/{instiId}")
    public ResponseEntity<List<BorrowItem>> getBorrowItemsByInstiId(@PathVariable String instiId) {
        return ResponseEntity.ok(borrowItemService.getBorrowItemsByInstiId(instiId));
    }

    @PostMapping("/addBulkBorrowItems")
    public ResponseEntity<?> addBulkBorrowItems(
            @RequestParam String instiId,
            @RequestBody List<BorrowItem> items) {

        try {
            // 🆕 Generate a unique Borrowed ID for this transaction (date + UUID)
            String borrowedId = "B" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + UUID.randomUUID().toString().substring(0, 6);

            for (BorrowItem item : items) {
                item.setBorrowedId(borrowedId); // 🆕 Assign same borrowed ID
                item.setInstiId(instiId);
                item.setBorrowedDate(new Date()); // Set current date
                borrowItemService.saveBorrowItem(item);
            }

            return ResponseEntity.ok("Borrow transaction successfully added with Borrowed ID: " + borrowedId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/addBorrowItems")
    public ResponseEntity<?> addBorrowItems(
            @RequestParam String instiId,
            @RequestParam String borrowedId, // ✅ Accept the borrowedId from frontend
            @RequestParam Long itemId,
            @RequestParam String itemName,
            @RequestParam String categoryName,
            @RequestParam Integer quantity,
            @RequestParam String status) {

        try {
            BorrowItem borrowItem = new BorrowItem();
            borrowItem.setInstiId(instiId);
            borrowItem.setBorrowedId(borrowedId);  // ✅ Set the same Borrowed ID
            borrowItem.setItemId(itemId);
            borrowItem.setItemName(itemName);
            borrowItem.setCategoryName(categoryName);
            borrowItem.setQuantity(quantity);
            borrowItem.setStatus(status);
            borrowItem.setBorrowedDate(new Date());

            borrowItemService.saveBorrowItem(borrowItem);

            return ResponseEntity.ok("Borrow item successfully added.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}



