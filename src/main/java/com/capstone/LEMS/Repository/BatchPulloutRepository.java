// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Repository/BatchPulloutRepository.java
package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BatchPulloutEntity;
import com.capstone.LEMS.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BatchPulloutRepository extends JpaRepository<BatchPulloutEntity, Integer> {

    // Custom query to find distinct pullout transactions by date and user
    // This will be used to group pullouts in the frontend similar to resupplies
    @Query("SELECT DISTINCT bp.datePullout, bp.pulledBy FROM BatchPulloutEntity bp")
    List<Object[]> findDistinctByDatePulloutAndPulledBy();

    // Custom query to find pullout batches by date and the user who pulled them out
    List<BatchPulloutEntity> findByDatePulloutAndPulledBy(LocalDate datePullout, UserEntity pulledBy);
}