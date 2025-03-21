package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowItem;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.UserRepository;
import com.capstone.LEMS.Service.BorrowItemService;

import com.capstone.LEMS.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/borrowitem")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class BorrowItemController {

    @Autowired
    private BorrowItemService borrowItemService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<BorrowItem> getAllBorrowItems() {
        return borrowItemService.getAllItems();
    }

    @GetMapping("/uid/{uid}")
    public ResponseEntity<List<BorrowItem>> getBorrowItemsByUid(@PathVariable int uid) {
        return ResponseEntity.ok(borrowItemService.getBorrowItemsByUid(uid));
    }


    @PostMapping("/addBorrowItems")
    public ResponseEntity<?> addBorrowItems(
            @RequestParam String uid,
            @RequestParam String borrowedId, // ✅ Accept the borrowedId from frontend
            @RequestParam Long itemId,
            @RequestParam String itemName,
            @RequestParam String categoryName,
            @RequestParam Integer quantity,
            @RequestParam String status) {

        try {
            UserEntity user = userRepository.findByInstiId(uid);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            BorrowItem borrowItem = new BorrowItem();
            borrowItem.setUser(user); // ✅ Set the user
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

    @PutMapping("/updateStatus/{borrowedId}")
    public ResponseEntity<String> updateStatusByBorrowedId(@PathVariable String borrowedId, @RequestParam String status) {
        try {
            borrowItemService.updateStatusByBorrowedId(borrowedId, status);
            return ResponseEntity.ok("Status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


}



