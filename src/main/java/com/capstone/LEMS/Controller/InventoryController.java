package com.capstone.LEMS.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Service.InventoryService;
@RestController
@RequestMapping("/inventory")
@CrossOrigin

public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    @GetMapping("/message")
    public String testMessage() {
        return "inventoryService is working";
    }
    @PostMapping("/getAllInventory")
    public List<InventoryEntity> getAllInventory(@RequestBody InventoryEntity inventoryEntity) {
        return inventoryService.getAllInventory();
    }
}