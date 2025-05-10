package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.TransactionHistory; // Import TransactionHistory
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.BatchResupplyRepository;
import com.capstone.LEMS.Repository.UserRepository;
import com.capstone.LEMS.Service.TransactionHistoryService; // Import TransactionHistoryService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class BatchResupplyService {

    @Autowired
    private BatchResupplyRepository batchResupplyRepository;
    
    @Autowired
    UserRepository userrepo;
    
    @Autowired
    private TransactionHistoryService transactionHistoryService; // Add TransactionHistoryService

    public BatchResupplyEntity addBatchResupply(BatchResupplyEntity batchResupply) {
        UserEntity user = userrepo.findById(batchResupply.getAddedBy().getUid()).orElse(null);
        batchResupply.setAddedBy(user);
        BatchResupplyEntity savedBatchResupply = batchResupplyRepository.save(batchResupply);

        // Log resupply transaction
        TransactionHistory transaction = new TransactionHistory();
        transaction.setItemId(batchResupply.getItemId());
        transaction.setUserId(batchResupply.getAddedBy().getUid());
        transaction.setTransactionType("resupply");
        transaction.setTransactionDate(new Date());
        transaction.setDetails("Resupplied item: " + batchResupply.getItemName());
        transactionHistoryService.saveTransactionHistory(transaction);

        return savedBatchResupply;
    }

    public List<BatchResupplyEntity> getAllBatchResupplies() {
        return batchResupplyRepository.findAll();
    }
    
    public ResponseEntity<?> getByLocalDateAndAddedBy(LocalDate dateResupply, int userID){
        UserEntity user = userrepo.findById(userID).orElse(null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(batchResupplyRepository.findByDateResupplyAndAddedBy(dateResupply, user));
    }
    
    public ResponseEntity<?> getAllDisctinct(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(batchResupplyRepository.findDistinctByDateResupplyAndAddedBy());
    }
}
