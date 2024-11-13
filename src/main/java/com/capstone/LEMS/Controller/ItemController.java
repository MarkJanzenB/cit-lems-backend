package com.capstone.LEMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Service.ItemService;

@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {
    @Autowired
    ItemService itemserv;

    @GetMapping("/message")
    public String testMessage() {
        return "ItemController is working";
    }

    @PostMapping("/insertitem")
    public ItemEntity addItem(@RequestBody ItemEntity item) {
        return itemserv.AddItem(item);
    }

    @GetMapping("/getallitems")
    public List<ItemEntity> getAllItems(){
        return itemserv.getAllItems();
    }
}
