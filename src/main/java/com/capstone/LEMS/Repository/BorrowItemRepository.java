package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BorrowItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, Long> {
}