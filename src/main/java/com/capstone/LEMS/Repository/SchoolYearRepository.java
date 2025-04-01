package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.SchoolYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYearEntity, Integer> {
}