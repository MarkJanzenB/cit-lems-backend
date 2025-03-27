package com.capstone.LEMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.ScheduleAssignmentEntity;
import com.capstone.LEMS.Service.ScheduleAssignmentService;

@RestController
@RequestMapping("/scheduleassignment")
@CrossOrigin
public class ScheduleAssignmentController {
	@Autowired
	ScheduleAssignmentService saserv;
	
	
	//for testing purposes
	@GetMapping("/message")
	public String testMessage() {
		return "ScheduleAssignmentController is working";
	}
	
	//for adding new schedule assignment
	@PostMapping("/insertscheduleassignment")
	public ResponseEntity<?> insertScheduleAssignment(@RequestBody ScheduleAssignmentEntity sa) {
		return saserv.insertScheduleAssignment(sa);
	}
	
	
	//get all schedule assignment
	@GetMapping("/getallscheduleassignment")
	public List<ScheduleAssignmentEntity> getAllScheduleAssignment(){
		return saserv.getAllScheduleAssignment();
	}
}
