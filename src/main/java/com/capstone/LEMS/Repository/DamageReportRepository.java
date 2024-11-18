package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.DamageReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReportEntity, Long> {
}