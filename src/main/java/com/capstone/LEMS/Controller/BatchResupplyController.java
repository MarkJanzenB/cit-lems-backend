package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.TransactionHistory;
import com.capstone.LEMS.Service.BatchResupplyService;
import com.capstone.LEMS.Service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/batchresupply")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class BatchResupplyController {

    @Autowired
    private BatchResupplyService batchResupplyService;

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @GetMapping("/combinedresupplyhistory")
    public ResponseEntity<?> getCombinedResupplyHistory() {
        return batchResupplyService.getCombinedResupplyHistory();
    }
    @PostMapping("/add")
    public BatchResupplyEntity addBatchResupply(@RequestBody BatchResupplyEntity batchResupply) {
        BatchResupplyEntity savedResupply = batchResupplyService.addBatchResupply(batchResupply);

        // Log resupply transaction
        TransactionHistory transaction = new TransactionHistory();
        transaction.setItemId(savedResupply.getItemId());
        transaction.setUserId(batchResupply.getAddedBy().getUid());
        transaction.setTransactionType("resupply");
        transaction.setTransactionDate(new Date());
        transaction.setDetails("Resupplied item: " + savedResupply.getItemName());
        transactionHistoryService.saveTransactionHistory(transaction);

        return savedResupply;
    }

    @GetMapping("/all")
    public List<BatchResupplyEntity> getAllBatchResupplies() {
        return batchResupplyService.getAllBatchResupplies();
    }

    @GetMapping("/getbydateanduser")
    public ResponseEntity<?> getByLocalDateAndAddedBy(@RequestParam LocalDate dateResupply, @RequestParam int userID){
        return batchResupplyService.getByLocalDateAndAddedBy(dateResupply, userID);
    }

    @GetMapping("/getalldistinct")
    public ResponseEntity<?> getAllDisctinct(){
        return batchResupplyService.getAllDisctinct();
    }
}