package com.capstone.LEMS.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.capstone.LEMS.Entity.DamageReportEntity;
import com.capstone.LEMS.Service.DamageReportService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/damages")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:*"})
public class DamageReportController {

    @Autowired
    private DamageReportService damageReportService;

    @GetMapping
    public List<DamageReportEntity> getAllDamageReports() {
        return damageReportService.getAllDamageReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DamageReportEntity> getDamageReportById(@PathVariable Long id) {
        Optional<DamageReportEntity> damageReport = damageReportService.getDamageReportById(id);
        return damageReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addDamages(@RequestBody List<DamageReportEntity> damages) {
        damageReportService.saveAllDamageReports(damages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDamageReport(@PathVariable Long id) {
        damageReportService.deleteDamageReport(id);
        return ResponseEntity.noContent().build();
    }
}