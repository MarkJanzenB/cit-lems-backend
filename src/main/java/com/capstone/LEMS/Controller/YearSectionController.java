package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.YearSectionEntity;
import com.capstone.LEMS.Service.YearSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/yearsection")
@CrossOrigin
public class YearSectionController {
    @Autowired
    YearSectionService yrsecserv;

    @GetMapping
    public ResponseEntity<?> getAllYearSections() {
        return yrsecserv.getAllYearSections();
    }

    @GetMapping("/{yearId}")
    public ResponseEntity<?> getYearSectionById(@PathVariable int yearId) {
        return yrsecserv.getYearSectionById(yearId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getYearSectionsByYearAndSection(
            @RequestParam String year,
            @RequestParam String section) {
        return yrsecserv.getYearSectionsByYearAndSection(year, section);
    }

//    @GetMapping("/active")
//    public ResponseEntity<?> getActiveYearSections() {
//        return yrsecserv.getActiveYearSections();
//    }

    @PostMapping
    public ResponseEntity<?> createYearSection(@RequestBody YearSectionEntity yearSection) {
        return yrsecserv.createYearSection(yearSection);
    }

    @PutMapping("/{yearId}")
    public ResponseEntity<?> updateYearSection(
            @PathVariable int yearId,
            @RequestBody YearSectionEntity yearSection) {
        return yrsecserv.updateYearSection(yearId, yearSection);
    }

    @DeleteMapping("/{yearId}")
    public ResponseEntity<?> deleteYearSection(@PathVariable int yearId) {
        return yrsecserv.deleteYearSection(yearId);
    }
}
