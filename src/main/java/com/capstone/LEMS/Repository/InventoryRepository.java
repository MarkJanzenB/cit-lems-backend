package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.InventoryEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
	List<InventoryEntity> findByItemCategoryCategoryId(int categoryId);
	
	InventoryEntity findByNameIgnoreCase(String name);
	Optional<InventoryEntity> findByName(String name);
}
