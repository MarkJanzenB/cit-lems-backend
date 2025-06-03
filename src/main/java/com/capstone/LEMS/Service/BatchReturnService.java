package com.capstone.LEMS.Service;

import com.capstone.LEMS.DTO.ItemStatusDTO;
import com.capstone.LEMS.Entity.BatchReturnEntity;
import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Entity.InventoryEntity;
import com.capstone.LEMS.Entity.ItemEntity;
import com.capstone.LEMS.Entity.PreparingItemEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.BatchReturnRepository;
import com.capstone.LEMS.Repository.DamageReportRepository;
import com.capstone.LEMS.Repository.ItemRepository;
import com.capstone.LEMS.Repository.PreparingItemRepository;
import com.capstone.LEMS.Repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class BatchReturnService {

    @Autowired
    private BatchReturnRepository batchReturnRepository;
    
    @Autowired
    private PreparingItemRepository prepirepo;
    
    @Autowired
    private UserRepository userrepo;
    
    @Autowired
    private ItemRepository itemrepo;
    
    @Autowired
    private DamageReportRepository damagerepo;
    
    @Transactional
    public void addBatchReturn(int uid, String referenceCode, Map<Integer, String> itemStatuses) {
    	List<PreparingItemEntity> preparingItems = prepirepo.findByReferenceCode(referenceCode);
    	UserEntity receivedBy = userrepo.findById(uid).orElseThrow();
    	
    	/*
    	 * Adds a new entry record for table Batch Return
    	 * */
    	for(PreparingItemEntity prepItem : preparingItems) {
        	BatchReturnEntity batch = new BatchReturnEntity();
        	batch.setDateReturned(LocalDate.now());
        	batch.setPreparingItem(prepItem);
        	batch.setReceivedBy(receivedBy);
        	prepItem.setStatus("Returned");
        	prepirepo.save(prepItem);
        	batchReturnRepository.save(batch);
    	}
    	
    	/*
    	 * Check all the items for that preparing item entry record
    	 * */
    	for(Map.Entry<Integer, String> entry : itemStatuses.entrySet()) {
    		Integer itemId = entry.getKey();
    		String status = entry.getValue();
    		
    		/*
    		 * Adds quantity to inventory if the item is returned in good condition
    		 * Meaning it can still be used and borrowed
    		 * */
    		ItemEntity item = itemrepo.findById(itemId).orElseThrow();
    		if("Available".equals(status)) {
    			InventoryEntity inventory = item.getInventory();
    			inventory.setQuantity(inventory.getQuantity() + 1);
    		}
    		item.setStatus(status);
    		itemrepo.save(item);
			//TODO: Marks
    		//Add checker if status is Available or Damaged
    		//If status is available: TH type returned in good condition or setTransactionType(status)
			//If status is not available: TH type returned in bad condition or setTransactionType(status)
    		//Pwede ma combined like or setTransactionType("Returned w/" + status);
    		//Di ko sure kung ma print apil ang slash
    		
    		/*
    		 * Creates an entry record in Damage Report table if the item is returned with Damage status
    		 * */
    		if("Damage".equals(status)) {
    			PreparingItemEntity preparingItem = preparingItems.get(0);
    			DamageReportEntity damageReport = new DamageReportEntity();
    			// Needed to convert Date to Local Date in TeacherScheduleEntity
    			// This would have been shorter if the Data type of Date attribute is Local Date instead of Date
    			// Date data type is useless in TeacherScheduleEntity since we have a separate attribute for start time and end time
    			damageReport.setDateBorrowed(preparingItem.getTeacherSchedule().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    			damageReport.setSubject(preparingItem.getTeacherSchedule().getSubject().getSubjectName());
    			damageReport.setYearSec(preparingItem.getTeacherSchedule().getYearSection());
    			damageReport.setInstructor(preparingItem.getUser().getLname());
    			// TODO:
    			// remove useless attributes for damage report once 100% done in this project
    			damagerepo.save(damageReport);
    		}
    	}
    }

    public List<BatchReturnEntity> getAllBatchReturns() {
        return batchReturnRepository.findAll();
    }
}