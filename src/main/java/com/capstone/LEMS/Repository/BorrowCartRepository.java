package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BorrowCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowCartRepository extends JpaRepository<BorrowCart, Long> {

    List<BorrowCart> findByInstiId(Long instiId);
    BorrowCart findByItemId(int itemId);
}