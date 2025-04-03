package com.capstone.LEMS.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Service.TeacherScheduleService;

@RestController
@RequestMapping("/teacherschedule")
@CrossOrigin
public class TeacherScheduleController {
    @Autowired
    TeacherScheduleService tcherschedserv;
    @GetMapping("/message")
    public String testMessage() {
        return "teacherScheduleService is working";
    }
    @GetMapping("/getTeacherScheds")
    public ResponseEntity<?> getTeacherScheds() {
        return ResponseEntity.ok(tcherschedserv.getAllTeacherSchedules());
    }
    @PostMapping("/getAllTeacherSchedules")
    public List<TeacherScheduleEntity> getAllTeacherSchedules(@RequestBody TeacherScheduleEntity teacherScheduleEntity) {
        return tcherschedserv.getAllTeacherSchedules();
    }
    @PostMapping("/addtsched")
    public TeacherScheduleEntity AddTeacherSchedule(@RequestBody TeacherScheduleEntity teachsched) {
    	return tcherschedserv.AddTeacherSchedule(teachsched);
    }
   @GetMapping("/teacher/{teacherId}")
   public ResponseEntity<?> getTSchedByTeacherId(@PathVariable int teacherId){
       return tcherschedserv.getSchedulesByTeacherId(teacherId);
   }

}
