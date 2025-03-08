package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Service.BorrowItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

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

    @PostMapping("/addBorrowItems")
    public ResponseEntity<?> addBorrowItems(
                @RequestParam String instiId, // Ensure this is a String
                @RequestParam Long itemId,
                @RequestParam String itemName,
                @RequestParam String categoryName,
                @RequestParam Integer quantity,
                @RequestParam String status) {

            try {
                BorrowItem borrowItem = new BorrowItem();
                borrowItem.setInstiId(instiId);  // Store as String
                borrowItem.setItemId(itemId);
                borrowItem.setItemName(itemName);
                borrowItem.setCategoryName(categoryName);
                borrowItem.setQuantity(quantity);
                borrowItem.setStatus(status);

                borrowItemService.saveBorrowItem(borrowItem);

                return ResponseEntity.ok("Borrow item successfully added.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
            }
        }
    }



