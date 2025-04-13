package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Service.PreparingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preparing-items")
public class PreparingItemController {

    @Autowired
    private PreparingItemService preparingItemService;

    // ✅ ADD THIS: To accept item from borrow cart and move to preparing_item table
    @PostMapping("/addToPreparingItem")
    public PreparingItemEntity addToPreparingItem(
            @RequestParam String uid,
            @RequestParam String itemName,
            @RequestParam String categoryName,
            @RequestParam int quantity,
            @RequestParam String status
    ) {
        return preparingItemService.addToPreparingItem(uid, itemName, categoryName, quantity, status);
    }

    // Finalize preparing item and validate unique_id
    @PutMapping("/finalize/{id}")
    public void finalizePreparingItem(@PathVariable int id, @RequestParam String uniqueId) {
        preparingItemService.finalizePreparingItem(id, uniqueId);
    }

    // Checkout preparing item and mark it as Borrowed
    @PutMapping("/checkout/{id}")
    public void checkoutPreparingItem(@PathVariable int id) {
        preparingItemService.checkoutPreparingItem(id);
    }

    // Fetch preparing items by institution UID
    @GetMapping("/uid/{uid}")
    public List<PreparingItemEntity> getPreparingItemsByUid(@PathVariable String uid) {
        return preparingItemService.getPreparingItemsByUid(uid);
    }

    // Fetch all preparing items
    @GetMapping("/all")
    public List<PreparingItemEntity> getAllPreparingItems() {
        return preparingItemService.getAllPreparingItems();
    }

    // Fetch preparing items with teacher name by institution ID
    @GetMapping("/with-teacher/{instiId}")
    public List<Object[]> getPreparingItemsWithTeacherName(@PathVariable String instiId) {
        return preparingItemService.getPreparingItemsWithTeacherName(instiId);
    }

    @GetMapping("/unique-ids")
    public List<String> getUniqueItemIds() {
        return preparingItemService.getAllUniqueItemIds();
    }

}
