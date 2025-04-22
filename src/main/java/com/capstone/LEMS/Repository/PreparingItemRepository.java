package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.PreparingItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Make sure you import UserEntity or the relevant class to use in the query if needed
import com.capstone.LEMS.Entity.UserEntity;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PreparingItemRepository extends JpaRepository<PreparingItemEntity, Integer> {
    List<PreparingItemEntity> findByInstiIdAndStatus(String instiId, String status);
    List<PreparingItemEntity> findByStatus(String status);
}
