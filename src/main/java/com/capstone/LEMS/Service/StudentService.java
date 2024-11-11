package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.StudentEntity;
import com.capstone.LEMS.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository strepo;

    //add student
    public StudentEntity addStudent(StudentEntity student){
        return strepo.save(student);
    }
    //display all students
    public List<StudentEntity> getAllStudent(){
        return strepo.findAll();
    }
    //update student
    public StudentEntity updateStudent(StudentEntity updatedStudent, int student_id){
        StudentEntity existingStudent = strepo.findById(student_id).orElseThrow(()-> new IllegalArgumentException("Student ID not found."));

        existingStudent.setSchool_id(updatedStudent.getSchool_id());
        existingStudent.setStudent_name(updatedStudent.getStudent_name());
        return strepo.save(existingStudent);
    }
    //delete student
    public void deleteStudent(int student_id){
        StudentEntity student = strepo.findById(student_id).orElseThrow(()-> new IllegalArgumentException("Student ID not found."));
        strepo.deleteById(student_id);
    }
}
