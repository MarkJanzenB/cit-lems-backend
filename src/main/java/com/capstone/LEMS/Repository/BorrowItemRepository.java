package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capstone.LEMS.Entity.BorrowItem;
import java.util.List;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, Long> {
    List<BorrowItem> findByInstiId(String instiId); // ✅ Ensure instiId is a String
    List<BorrowItem> findByBorrowedId(String borrowedId); // ✅ Fetch by borrowed ID

}

