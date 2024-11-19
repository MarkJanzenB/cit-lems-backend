package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capstone.LEMS.Entity.DamageReportEntity;

public interface DamageReportRepository extends JpaRepository<DamageReportEntity, Long> {
}