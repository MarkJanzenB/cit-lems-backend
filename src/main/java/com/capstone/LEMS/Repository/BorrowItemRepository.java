package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.capstone.LEMS.Entity.BorrowItem;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, Long> {

    // ✅ Fetch borrow items by institution ID (instiId) with user details
//    @Query("SELECT b FROM BorrowItem b JOIN FETCH b.user WHERE b.instiId = :instiId ORDER BY b.borrowedDate DESC")
//    List<BorrowItem> findByInstiIdWithUser(@Param("instiId") String instiId);

    // ✅ Fetch borrow items by institution ID (without user details)
//    List<BorrowItem> findByInstiId(String instiId);

    // ✅ Fetch borrow items by borrowed ID
    List<BorrowItem> findByBorrowedId(String borrowedId);

    List<BorrowItem> findByUser_UserId(int userId);

}
