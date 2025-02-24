package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BorrowCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowCartRepository extends JpaRepository<BorrowCart, Long> {

    List<BorrowCart> findByInstiId(String instiId);
    BorrowCart findByItemId(int itemId);
    @Query("SELECT b FROM BorrowCart b WHERE b.itemId = :itemId AND b.instiId = :instiId")
    BorrowCart findByItemIdAndInstiIdStrict(@Param("itemId") int itemId, @Param("instiId") String instiId);
    void deleteByInstiId(String instiId);

}