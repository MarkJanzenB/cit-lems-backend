package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Service.DamageReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/damage-reports")
public class DamageReportController {
    @Autowired
    private DamageReportService damageReportService;

    @GetMapping
    public List<DamageReportEntity> getAllDamageReports() {
        return damageReportService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DamageReportEntity> getDamageReportById(@PathVariable Long id) {
        Optional<DamageReportEntity> damageReport = damageReportService.findById(id);
        return damageReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DamageReportEntity createDamageReport(@RequestBody DamageReportEntity damageReport) {
        return damageReportService.save(damageReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DamageReportEntity> updateDamageReport(@PathVariable Long id, @RequestBody DamageReportEntity damageReportDetails) {
        Optional<DamageReportEntity> damageReport = damageReportService.findById(id);
        if (damageReport.isPresent()) {
            DamageReportEntity report = damageReport.get();
            report.setDescription(damageReportDetails.getDescription());
            report.setStatus(damageReportDetails.getStatus());
            return ResponseEntity.ok(damageReportService.save(report));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamageReport(@PathVariable Long id) {
        if (damageReportService.findById(id).isPresent()) {
            damageReportService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}