package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.TransactionHistory;
import com.capstone.LEMS.Service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactionhistory")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class TransactionHistoryController {

    @Autowired
    private TransactionHistoryService transactionHistoryService;

    // Get all transaction histories
    @GetMapping
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistories() {
        List<TransactionHistory> histories = transactionHistoryService.getAllTransactionHistories();
        return ResponseEntity.ok(histories);
    }

}
