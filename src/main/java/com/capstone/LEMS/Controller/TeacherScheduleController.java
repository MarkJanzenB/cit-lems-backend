package com.capstone.LEMS.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Service.TeacherScheduleService;

@RestController
@RequestMapping("/teacherschedule")
@CrossOrigin
public class TeacherScheduleController {
    @Autowired
    TeacherScheduleService teacherScheduleService;
    @GetMapping("/message")
    public String testMessage() {
        return "teacherScheduleService is working";
    }
    @PostMapping("/getAllTeacherSchedules")
    public List<TeacherScheduleEntity> getAllTeacherSchedules(@RequestBody TeacherScheduleEntity teacherScheduleEntity) {
        return teacherScheduleService.getAllTeacherSchedules();
    }
    
    @PostMapping("/insertteacherschedule")
    public TeacherScheduleEntity AddTeacherSchedule(@RequestBody TeacherScheduleEntity teachsched) {
    	return teacherScheduleService.AddTeacherSchedule(teachsched);
    }
}
