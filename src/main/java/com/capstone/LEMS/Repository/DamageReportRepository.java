package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Entity.YearSectionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageReportRepository extends JpaRepository<DamageReportEntity, Long> {
}