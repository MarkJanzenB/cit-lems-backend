package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ScheduleAssignmentEntity;
import com.capstone.LEMS.Repository.ScheduleAssignmentRepository;

@Service
public class ScheduleAssignmentService {
	@Autowired
	ScheduleAssignmentRepository sarepo;
	
	//insert a schedule assignment
	public ScheduleAssignmentEntity insertScheduleAssignment(ScheduleAssignmentEntity sa) {
		return sarepo.save(sa);
	}
	
	//get all schedule assignment data
	public List<ScheduleAssignmentEntity> getAllScheduleAssignment(){
		return sarepo.findAll();
	}
}
