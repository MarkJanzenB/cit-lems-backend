package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {

}
