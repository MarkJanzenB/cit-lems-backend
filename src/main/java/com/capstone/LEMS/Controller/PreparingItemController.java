package com.capstone.LEMS.Controller;

import com.capstone.LEMS.DTO.CheckoutRequestDTO;
import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Service.PreparingItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preparing-items")
@CrossOrigin
public class PreparingItemController {

    @Autowired
    private PreparingItemService preparingItemService;

    /*
     *REFACTOR: params is too long and looks unclean, use DTO instead.
     * */
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

    /*
     * REFACTOR: change naming convention for the url
     * currently its misleading
     * */
    // Fetch preparing items by institution UID
    @GetMapping("/getpreparingitems")
    public List<PreparingItemEntity> getPreparingItems(@RequestParam(required = false) String instiId, @RequestParam String status) {
        return preparingItemService.getPreparingItems(instiId, status);
    }

    @PutMapping("/proceedtocheckout")
    public void proceedToCheckOut(@RequestBody CheckoutRequestDTO checkoutRequest) {
    	preparingItemService.proceedToCheckOut(checkoutRequest.getItemQuantities(), checkoutRequest.getUniqueIdsMap());
    }
    
}
