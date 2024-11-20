package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Repository.DamageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DamageReportService {
    @Autowired
    private DamageReportRepository damageReportRepository;

    public List<DamageReportEntity> findAll() {
        return damageReportRepository.findAll();
    }

    public Optional<DamageReportEntity> findById(Long id) {
        return damageReportRepository.findById(id);
    }

    public DamageReportEntity save(DamageReportEntity damageReport) {
        return damageReportRepository.save(damageReport);
    }

    public void deleteById(Long id) {
        damageReportRepository.deleteById(id);
    }
}