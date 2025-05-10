// File: src/main/java/com/capstone/LEMS/Controller/BorrowCartController.java
package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Service.BorrowCartService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrowcart")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
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
                                                            @RequestParam int quantity,
                                                            @RequestParam(required = false) String variant) { // Receive the variant
        try {
            BorrowCartEntity borrowCartEntity = borrowCartService.addToBorrowCart(instiId, itemName, categoryName, quantity, variant);
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

    @PostMapping("/finalize/{instiId}")
    public ResponseEntity<String> finalizeBorrowCart(@PathVariable String instiId,
                                                     @RequestBody Map<String, Integer> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Integer teacherScheduleId = requestBody.get("teacherScheduleId");
        if (teacherScheduleId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: teacherScheduleId is required.");
        }

        try {
            borrowCartService.moveToPreparingItem(instiId, teacherScheduleId);
            return ResponseEntity.ok("Items moved to 'preparing_item' successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/teacherSchedule/{instiId}")
    public ResponseEntity<List<TeacherScheduleEntity>> getTeacherSchedules(@PathVariable String instiId) {
        try {
            List<TeacherScheduleEntity> schedules = borrowCartService.getTeacherScheduleByInstiId(instiId);
            return new ResponseEntity<>(schedules, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(java.util.Collections.emptyList(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBorrowCartQuantity(@PathVariable int id,
                                                           @RequestBody Map<String, Integer> requestBody,
                                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Authorization header received.");
        }

        Integer newQuantity = requestBody.get("quantity");
        if (newQuantity == null || newQuantity < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Invalid quantity provided.");
        }

        try {
            borrowCartService.updateItemQuantity(id, newQuantity); // <--- This is where the error is
            return ResponseEntity.ok("Borrow cart item quantity updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating quantity: " + e.getMessage());
        }
    }

    @GetMapping("/variants")
    public ResponseEntity<List<String>> getVariantsByItemName(@RequestParam String itemName) {
        try {
            List<String> variants = borrowCartService.getAvailableVariants(itemName);
            return ResponseEntity.ok(variants);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/updateVariant/{id}")
    public ResponseEntity<String> updateBorrowCartVariant(@PathVariable int id,
                                                          @RequestBody Map<String, String> requestBody,
                                                          @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No Authorization header received.");
        }

        String newVariant = requestBody.get("variant");
        if (newVariant == null || newVariant.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Variant cannot be empty.");
        }

        try {
            borrowCartService.updateItemVariant(id, newVariant); // Call the service method
            return ResponseEntity.ok("Borrow cart item variant updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating variant: " + e.getMessage());
        }
    }
}