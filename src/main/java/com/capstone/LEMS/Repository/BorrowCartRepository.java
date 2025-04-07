package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowCartRepository extends JpaRepository<BorrowCartEntity, Integer> {
    // Find all borrow carts by institution ID
    List<BorrowCartEntity> findByInstiId(String instiId);

    // Delete all borrow carts by institution ID
    void deleteByInstiId(String instiId);

    // Custom query method to find BorrowCartEntity by itemName and instiId
    BorrowCartEntity findByItemNameAndInstiId(String itemName, String instiId);

    // Optionally, you can add more methods as needed
}
