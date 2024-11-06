package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.StudentEntity;
import com.capstone.LEMS.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService stserv;

    @PostMapping("/addstudent")
    public StudentEntity addStudent(@RequestBody StudentEntity student){
        return stserv.addStudent(student);
    }
    @GetMapping("/getallstudent")
    public List<StudentEntity> getAllStudent(){
        return stserv.getAllStudent();
    }
    @PutMapping("/updatestudent/{student_id}")
    public StudentEntity updateStudent(@RequestBody StudentEntity student, @PathVariable int student_id){
        return stserv.updateStudent(student,student_id);
    }
    @DeleteMapping("/deleteStudent/{student_id}")
    public void deleteStudent(@PathVariable int student_id){
        stserv.deleteStudent(student_id);
    }


}
