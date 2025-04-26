package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.TransactionHistory;
import com.capstone.LEMS.Repository.TransactionHistoryRepository;
import com.capstone.LEMS.Entity.BorrowItemEntity;
import com.capstone.LEMS.Entity.BatchResupplyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistory saveTransactionHistory(TransactionHistory transactionHistory) {
        return transactionHistoryRepository.save(transactionHistory);
    }

    public List<TransactionHistory> getAllTransactionHistories() {
        return transactionHistoryRepository.findAll();
    }
}
