package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
}
