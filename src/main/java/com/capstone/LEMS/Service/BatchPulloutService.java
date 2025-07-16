// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Service/BatchPulloutService.java
package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchPulloutEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.BatchPulloutRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BatchPulloutService {

    @Autowired
    private BatchPulloutRepository batchPulloutRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    public ResponseEntity<?> getCombinedPulloutHistory() {
        List<Object[]> distinctPullouts = batchPulloutRepo.findDistinctByDatePulloutAndPulledBy();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] entry : distinctPullouts) {
            LocalDate date = (LocalDate) entry[0];
            UserEntity user = (UserEntity) entry[1];

            List<BatchPulloutEntity> batches = batchPulloutRepo.findByDatePulloutAndPulledBy(date, user);
            List<ItemEntity> items = new ArrayList<>();

            for (BatchPulloutEntity batch : batches) {
                List<ItemEntity> pulledOutItems = itemRepo.findByBatchPullout(batch);
                items.addAll(pulledOutItems);
            }

            Map<String, List<ItemEntity>> groupedItems = items.stream()
                    .collect(Collectors.groupingBy(ItemEntity::getItemName));

            List<Map<String, Object>> itemDetails = new ArrayList<>();
            for (Map.Entry<String, List<ItemEntity>> group : groupedItems.entrySet()) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", group.getKey()); // Item common name
                itemData.put("quantity", group.getValue().size()); // Count of individual units pulled out

                List<Map<String, Object>> variants = group.getValue().stream().map(item -> {
                    Map<String, Object> variantData = new HashMap<>();
                    variantData.put("id", item.getItemId());
                    variantData.put("variantName", item.getVariant());
                    variantData.put("serialNumber", item.getUniqueId());
                    variantData.put("categoryName", item.getInventory() != null && item.getInventory().getItemCategory() != null ? item.getInventory().getItemCategory().getCategoryName() : "N/A");
                    variantData.put("status", item.getIsDeleted() ? "Pulled Out" : "N/A"); // Should always be true for these items
                    variantData.put("originalDateAdded", item.getDateAdded()); // NEW: Include original date added

                    return variantData;
                }).collect(Collectors.toList());

                itemData.put("variants", variants);
                itemDetails.add(itemData);
            }

            Map<String, Object> pulloutData = new HashMap<>();
            pulloutData.put("date", date);
            pulloutData.put("pulledBy", user.getFname() + " " + user.getLname());
            pulloutData.put("pulledById", user.getUid());
            pulloutData.put("role", user.getRole().getRoleName());
            pulloutData.put("items", itemDetails);
            response.add(pulloutData);
        }

        return ResponseEntity.ok(response);
    }
}