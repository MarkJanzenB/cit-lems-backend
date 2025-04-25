package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.SchoolYearEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYearEntity, Integer> {
    Optional<SchoolYearEntity> findByInstiId(String instiId);
}