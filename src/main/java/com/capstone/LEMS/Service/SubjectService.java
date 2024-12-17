package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.SubjectEntity;
import com.capstone.LEMS.Repository.SubjectRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository srepo;
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    //add subject
    public SubjectEntity addSubject(SubjectEntity subject){
        return srepo.save(subject);

    }
    //display all
    public List<SubjectEntity> getAllSubject(){
    	log.info("fetching all subjects");
        return srepo.findAll();
    }
    //update subject
//    public SubjectEntity updateSubject(SubjectEntity updatedSubject, int subject_id){
//        SubjectEntity existingSubject = srepo.findById(subject_id).orElseThrow(()-> new IllegalArgumentException("Subject ID not found."));
//
//        existingSubject.setSubjectName(updatedSubject.getSubjectName());
//        existingSubject.setYearSection(updatedSubject.getYearSection());
//        return srepo.save(existingSubject);
//    }

    //delete
    public void deleteSubject(int subject_id){
        SubjectEntity subject = srepo.findById(subject_id).orElseThrow(()-> new IllegalArgumentException("Subject ID not found."));
        srepo.deleteById(subject_id);

    }
}
