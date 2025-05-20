package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.TransactionHistory; // Import TransactionHistory
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.BatchResupplyRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.UserRepository;
import com.capstone.LEMS.Service.TransactionHistoryService; // Import TransactionHistoryService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchResupplyService {

    @Autowired
    private BatchResupplyRepository batchResupplyRepository;
    
    @Autowired
    UserRepository userrepo;
    
    @Autowired
    private TransactionHistoryService transactionHistoryService; // Add TransactionHistoryService
    @Autowired
    private ItemRepository itemRepository;

    public ResponseEntity<?> getCombinedResupplyHistory() {
        List<Object[]> distinctResupplies = batchResupplyRepository.findDistinctByDateResupplyAndAddedBy();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] entry : distinctResupplies) {
            LocalDate date = (LocalDate) entry[0];
            UserEntity user = (UserEntity) entry[1];

            // Fetch items for the given date and user
            List<BatchResupplyEntity> batches = batchResupplyRepository.findByDateResupplyAndAddedBy(date, user);
            List<ItemEntity> items = new ArrayList<>();
            for (BatchResupplyEntity batch : batches) {
                items.addAll(itemRepository.findByBatchResupply(batch));
            }

            // Group items by name
            Map<String, List<ItemEntity>> groupedItems = items.stream()
                    .collect(Collectors.groupingBy(ItemEntity::getItemName));

            List<Map<String, Object>> itemDetails = new ArrayList<>();
            for (Map.Entry<String, List<ItemEntity>> group : groupedItems.entrySet()) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", group.getKey());
                itemData.put("quantity", group.getValue().size());
                itemData.put("variants", group.getValue().stream().map(item -> {
                    Map<String, Object> variant = new HashMap<>();
                    variant.put("id", item.getItemId());
                    variant.put("name", item.getItemName());
                    variant.put("serialNumber", item.getUniqueId());
                    return variant;
                }).collect(Collectors.toList()));
                itemDetails.add(itemData);
            }

            // Add to response
            Map<String, Object> resupplyData = new HashMap<>();
            resupplyData.put("date", date);
            resupplyData.put("processedBy", user.getFname() + " " + user.getLname());
            resupplyData.put("processedById", user.getUid());
            resupplyData.put("role", user.getRole().getRoleName());
            resupplyData.put("items", itemDetails);
            response.add(resupplyData);
        }

        return ResponseEntity.ok(response);
    }


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
