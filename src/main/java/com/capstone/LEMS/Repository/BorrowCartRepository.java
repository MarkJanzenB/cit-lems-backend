package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BorrowCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowCartRepository extends JpaRepository<BorrowCartEntity, Integer> {

    List<BorrowCartEntity> findByInstiId(String instiId);
    BorrowCartEntity findByItemId(int itemId);
    @Query("SELECT b FROM BorrowCartEntity b WHERE b.itemId = :itemId AND b.instiId = :instiId")
    BorrowCartEntity findByItemIdAndInstiIdStrict(@Param("itemId") int itemId, @Param("instiId") String instiId);
    void deleteByInstiId(String instiId);

}