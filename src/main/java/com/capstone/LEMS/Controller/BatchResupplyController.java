package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Service.BatchResupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/batchresupply")
@CrossOrigin
public class BatchResupplyController {

    @Autowired
    private BatchResupplyService batchResupplyService;

    @PostMapping("/add")
    public BatchResupplyEntity addBatchResupply(@RequestBody BatchResupplyEntity batchResupply) {
        return batchResupplyService.addBatchResupply(batchResupply);
    }

    @GetMapping("/all")
    public List<BatchResupplyEntity> getAllBatchResupplies() {
        return batchResupplyService.getAllBatchResupplies();
    }
    
    @GetMapping("/getbydateanduser")
    public ResponseEntity<?> getByLocalDateAndAddedBy(@RequestParam LocalDate dateResupply, @RequestParam int userID){
    	return batchResupplyService.getByLocalDateAndAddedBy(dateResupply, userID);
    }
}