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
    @GetMapping("/getAllTeacherSchedules")
    public List<TeacherScheduleEntity> getAllTeacherSchedules() {
        return tcherschedserv.getAllTeacherSchedules();
    }
    @PostMapping("/addtsched")
    public TeacherScheduleEntity AddTeacherSchedule(@RequestBody TeacherScheduleEntity teachsched, @RequestParam int createdby) {
    	return tcherschedserv.AddTeacherSchedule(teachsched, createdby);
    }
   @GetMapping("/teacher/{teacherId}")
   public ResponseEntity<?> getTSchedByTeacherId(@PathVariable int teacherId){
       return tcherschedserv.getSchedulesByTeacherId(teacherId);
   }
   
   @PutMapping("/update")
   public ResponseEntity<?> updateSchedule(@RequestParam int teacherScheduleId, @RequestBody TeacherScheduleEntity teachsched){
	   return tcherschedserv.updateSchedule(teacherScheduleId, teachsched);
   }

}
