package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.SchoolYearEntity;
import com.capstone.LEMS.Repository.SchoolYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolYearService {

    @Autowired
    private SchoolYearRepository schoolYearRepository;

    public SchoolYearEntity addSchoolYear(SchoolYearEntity schoolYear) {
        return schoolYearRepository.save(schoolYear);
    }

    public List<SchoolYearEntity> getAllSchoolYears() {
        return schoolYearRepository.findAll();
    }
}