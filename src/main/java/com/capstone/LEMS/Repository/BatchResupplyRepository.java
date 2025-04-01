package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchResupplyRepository extends JpaRepository<BatchResupplyEntity, Integer> {
}