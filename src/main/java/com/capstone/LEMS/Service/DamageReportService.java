package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Entity.YearSectionEntity;
import com.capstone.LEMS.Repository.DamageReportRepository;
import com.capstone.LEMS.Repository.YearSectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageReportService {

    @Autowired
    private DamageReportRepository damageReportRepository;
    
    @Autowired
    private YearSectionRepository yearSecRepo;

    public List<DamageReportEntity> getAllDamageReports() {
        return damageReportRepository.findAll();
    }

    public DamageReportEntity getDamageReportById(Long id) {
        return damageReportRepository.findById(id).orElse(null);
    }

    public DamageReportEntity createDamageReport(DamageReportEntity damageReport) {
    	YearSectionEntity yearSec = yearSecRepo.findById(damageReport.getYearSec().getYrsecId()).orElseThrow();
    	damageReport.setYearSec(yearSec);
        return damageReportRepository.save(damageReport);
    }

    public DamageReportEntity updateDamageReport(Long id, DamageReportEntity damageReportDetails) {
        DamageReportEntity damageReport = damageReportRepository.findById(id).orElse(null);
        if (damageReport != null) {
            damageReport.setSerialNo(damageReportDetails.getSerialNo());
            damageReport.setItemName(damageReportDetails.getItemName());
            damageReport.setDateBorrowed(damageReportDetails.getDateBorrowed());
            damageReport.setSubject(damageReportDetails.getSubject());
            damageReport.setYearSec(damageReportDetails.getYearSec());
            damageReport.setInstructor(damageReportDetails.getInstructor());
            damageReport.setPhoto(damageReportDetails.getPhoto());
            damageReport.setAccountable(damageReportDetails.getAccountable());
            damageReport.setIncidentStatus(damageReportDetails.getIncidentStatus());
            return damageReportRepository.save(damageReport);
        }
        return null;
    }

    public void deleteDamageReport(Long id) {
        damageReportRepository.deleteById(id);
    }
}