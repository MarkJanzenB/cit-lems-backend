package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Service.BorrowItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/borrowitem")
@CrossOrigin // Adjust the origin to match your React app URL
public class BorrowItemController {

    @Autowired
    private BorrowItemService borrowItemService;

    @PostMapping("/addBorrowItems")
    public ResponseEntity<?> addBorrowItems(@RequestBody List<BorrowItem> borrowItems) {
        try {
            borrowItemService.addBorrowItems(borrowItems);
            return ResponseEntity.ok("Borrow items added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding borrow items: " + e.getMessage());
        }
    }


    @GetMapping("/getAllBorrowItems")
    public ResponseEntity<List<BorrowItem>> getAllBorrowItems() {
        return ResponseEntity.ok(borrowItemService.getAllBorrowItems());
    }
}