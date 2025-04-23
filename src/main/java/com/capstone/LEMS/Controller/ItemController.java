package com.capstone.LEMS.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Service.ItemService;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class ItemController {
    @Autowired
    ItemService itemserv;

    @GetMapping("/message")
    public String testMessage() {
        return "ItemController is working";
    }

    @PostMapping("/insertitem")
    public ResponseEntity<?> addItem(@RequestBody Map<String, Object> itemsToAdd, @RequestParam int bulkSize) {
        return itemserv.AddItem(itemsToAdd, bulkSize);
    }
    
    @PutMapping("/updateitems")
    public ResponseEntity<?> updateItems(@RequestParam String itemToEdit, @RequestBody ItemEntity newItemDetails){
    	return itemserv.updateItems(itemToEdit, newItemDetails);
    }
    
    @DeleteMapping("/deleteitems")
    public ResponseEntity<?> deleteItems(@RequestParam int bulkSize, @RequestBody ItemEntity itemsToDelete){
    	return itemserv.deleteItems(bulkSize, itemsToDelete);
    }

    @GetMapping("/getallitems")
    public List<ItemEntity> getAllItems(){
        return itemserv.getAllItems();
    }
    
    @PutMapping("/borrow")
    public ResponseEntity<?> borrowItem(@RequestBody Map<String, Object> request){
    	return itemserv.borrowItem(request);
    }
    
    @PutMapping("/return")
    public ResponseEntity<?> returnItem(@RequestBody List<Map<String, Object>> itemsRequest) {
        return itemserv.returnItem(itemsRequest);
    }
   
    @GetMapping("/resupplyhistory")
    public ResponseEntity<?> getResupplyHistory(@RequestParam LocalDate dateResupply, @RequestParam int uid){
    	return itemserv.getResupplyHistory(dateResupply, uid);
    }
    
    @GetMapping("/getuniqueids")
    public ResponseEntity<?> getListOfUniqueIDs(@RequestParam String itemName, @RequestParam(required = false) String category){
    	return itemserv.getListOfUniqueIDs(itemName, category);
    }
    
    @PostMapping("/getbypreparingids")
    public ResponseEntity<?> findByPreparingItemIds(@RequestBody List<Integer> preparingItemIds){
    	return itemserv.findByPreparingItemIds(preparingItemIds);
    }
}
