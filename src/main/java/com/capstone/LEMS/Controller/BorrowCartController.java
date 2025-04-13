// File: src/main/java/com/capstone/LEMS/Controller/BorrowCartController.java
package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Service.BorrowCartService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/borrowcart")
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin to match your React app URL
public class BorrowCartController {

    @Autowired
    private BorrowCartService borrowCartService;

    @Autowired
    private PreparingItemRepository preparingItemRepository;

    @GetMapping("/getAllBorrowCarts")
    public ResponseEntity<List<BorrowCartEntity>> getAllBorrowCarts() {
        return ResponseEntity.ok(borrowCartService.getAllBorrowCarts());
    }

    @PostMapping("/addToBorrowCart")
    public ResponseEntity<BorrowCartEntity> addToBorrowCart(@RequestParam String instiId,
                                                            @RequestParam String itemName,
                                                            @RequestParam String categoryName,
                                                            @RequestParam int quantity) {
        try {
            BorrowCartEntity borrowCartEntity = borrowCartService.addToBorrowCart(instiId, itemName, categoryName, quantity);
            return ResponseEntity.ok(borrowCartEntity);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/instiId/{instiId}")
    public ResponseEntity<List<BorrowCartEntity>> getBorrowCartsByInstId(@PathVariable String instiId) {
        return ResponseEntity.ok(borrowCartService.getBorrowCartsByInsti(instiId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrowCart(@PathVariable int id,
                                                   @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Authorization header received.");
        }

        try {
            borrowCartService.deleteBorrowCart(id);
            return ResponseEntity.ok("Item removed from borrow cart successfully.");
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

//    @PostMapping("/increase/{id}")
//    public ResponseEntity<String> increaseItemQuantity(@PathVariable int id) {
//        try {
//            borrowCartService.increaseItemQuantity(id);
//            return ResponseEntity.ok("Item quantity increased successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/decrease/{id}")
//    public ResponseEntity<String> decreaseItemQuantity(@PathVariable int id) {
//        try {
//            borrowCartService.decreaseItemQuantity(id);
//            return ResponseEntity.ok("Item quantity decreased successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
//        }
//    }


    @PostMapping("/finalize/{instiId}")
    public ResponseEntity<String> finalizeBorrowCart(@PathVariable String instiId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            borrowCartService.moveToPreparingItem(instiId);
            return ResponseEntity.ok("Items moved to 'preparing_item' successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
