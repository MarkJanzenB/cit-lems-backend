package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BatchReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchReturnRepository extends JpaRepository<BatchReturnEntity, Integer> {
}