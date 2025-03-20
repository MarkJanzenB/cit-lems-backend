// File: src/main/java/com/capstone/LEMS/Controller/BorrowCartController.java
package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowCart;
import com.capstone.LEMS.Service.BorrowCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowcart")
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin to match your React app URL
public class BorrowCartController {

    @Autowired
    private BorrowCartService borrowCartService;

    @GetMapping("/getAllBorrowCarts")
    public ResponseEntity<List<BorrowCart>> getAllBorrowCarts() {
        return ResponseEntity.ok(borrowCartService.getAllBorrowCarts());
    }

    @PostMapping("/addToBorrowCart")
    public ResponseEntity<BorrowCart> addToBorrowCart(@RequestParam String instiId, @RequestParam int itemId, @RequestParam String itemName, @RequestParam String categoryName, @RequestParam int quantity) {
        System.out.println("instiId: " + instiId);
        return ResponseEntity.ok(borrowCartService.addToBorrowCart(instiId, itemId, itemName, categoryName, quantity));
    }

    @GetMapping("/insti/{instiId}")
    public ResponseEntity<List<BorrowCart>> getBorrowCartsByInsti(@PathVariable String instiId) {
        return ResponseEntity.ok(borrowCartService.getBorrowCartsByInsti(instiId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrowCart(@PathVariable Long id, @RequestParam int quantity,
                                                   @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Authorization header received.");
        }

        System.out.println("Authorization Header: " + authorizationHeader); // Debugging step

        try {
            borrowCartService.restoreStockAndRemoveItem(id, quantity);
            return ResponseEntity.ok("Item removed from borrow cart and stock restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear/{instiId}")
    public ResponseEntity<String> clearBorrowCart(@PathVariable String instiId,
                                                  @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Authorization header received.");
        }

        try {
            borrowCartService.clearCart(instiId);
            return ResponseEntity.ok("Borrow cart cleared successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/increase/{id}")
    public ResponseEntity<String> increaseItemQuantity(@PathVariable Long id) {
        try {
            borrowCartService.increaseItemQuantity(id);
            return ResponseEntity.ok("Item quantity increased successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/decrease/{id}")
    public ResponseEntity<String> decreaseItemQuantity(@PathVariable Long id) {
        try {
            borrowCartService.decreaseItemQuantity(id);
            return ResponseEntity.ok("Item quantity decreased successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}