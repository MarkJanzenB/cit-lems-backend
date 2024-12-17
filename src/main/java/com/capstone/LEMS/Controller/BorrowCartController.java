package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BorrowCart;
import com.capstone.LEMS.Service.BorrowCartService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<BorrowCart> addToBorrowCart(@RequestParam int instiId, @RequestParam int itemId, @RequestParam String itemName, @RequestParam String categoryName, @RequestParam int quantity) {
        System.out.println("instiId: " + instiId);
        return ResponseEntity.ok(borrowCartService.addToBorrowCart(instiId, itemId, itemName, categoryName, quantity));
    }

    @GetMapping("/insti/{instiId}")
    public ResponseEntity<List<BorrowCart>> getBorrowCartsByInsti(@PathVariable Long instiId) {
        return ResponseEntity.ok(borrowCartService.getBorrowCartsByInsti(instiId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrowCart(@PathVariable Long id) {
        borrowCartService.deleteBorrowCart(id);
        return ResponseEntity.noContent().build();
    }
}