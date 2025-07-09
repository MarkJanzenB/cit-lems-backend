package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.BatchResupplyRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.UserRepository;

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
    private ItemRepository itemRepository;

    public ResponseEntity<?> getCombinedResupplyHistory() {
        List<Object[]> distinctResupplies = batchResupplyRepository.findDistinctByDateResupplyAndAddedBy();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] entry : distinctResupplies) {
            LocalDate date = (LocalDate) entry[0];
            UserEntity user = (UserEntity) entry[1];

            // Fetch items for the given date and user
            // Changed to findByBatchResupplyAndIsDeletedFalse
            List<BatchResupplyEntity> batches = batchResupplyRepository.findByDateResupplyAndAddedBy(date, user);
            List<ItemEntity> items = new ArrayList<>();
            for (BatchResupplyEntity batch : batches) {
                items.addAll(itemRepository.findByBatchResupplyAndIsDeletedFalse(batch));
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
                    // CORRECTED: Display variant name instead of item name
                    variant.put("name", item.getVariant());
                    variant.put("serialNumber", item.getUniqueId());
                    // Add category name to variant details for completeness
                    variant.put("categoryName", item.getInventory() != null && item.getInventory().getItemCategory() != null ? item.getInventory().getItemCategory().getCategoryName() : "N/A");
                    // Add isDeleted status for consistency
                    variant.put("isDeleted", item.getIsDeleted());
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