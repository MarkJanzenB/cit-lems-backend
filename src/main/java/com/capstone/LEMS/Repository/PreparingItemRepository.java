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

    // Fetch preparing items by institution ID
    List<PreparingItemEntity> findByInstiId(String instiId);

    // Custom query to join PreparingItemEntity and UserEntity by instiId and fetch teacher names
    @Query("SELECT p, u.firstName, u.lastName FROM PreparingItemEntity p " +
            "JOIN UserEntity u ON p.instiId = u.instiId " +
            "WHERE p.instiId = :instiId")
    List<Object[]> findPreparingItemsWithTeacherName(String instiId);  // Object[] holds both preparing items and teacher's name
}
