package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.SchoolYearEntity;
import com.capstone.LEMS.Service.SchoolYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schoolyear")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class SchoolYearController {

    @Autowired
    private SchoolYearService schoolYearService;

    @PostMapping("/add")
    public SchoolYearEntity addSchoolYear(@RequestBody SchoolYearEntity schoolYear) {
        return schoolYearService.addSchoolYear(schoolYear);
    }

    @GetMapping("/all")
    public List<SchoolYearEntity> getAllSchoolYears() {
        return schoolYearService.getAllSchoolYears();
    }
}