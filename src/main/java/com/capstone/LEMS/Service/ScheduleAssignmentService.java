package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ScheduleAssignmentEntity;
import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.ScheduleAssignmentRepository;
import com.capstone.LEMS.Repository.TeacherScheduleRepository;
import com.capstone.LEMS.Repository.UserRepository;

@Service
public class ScheduleAssignmentService {
	@Autowired
	ScheduleAssignmentRepository sarepo;
	
	@Autowired
	TeacherScheduleRepository tsrepo;
	
	@Autowired
	UserRepository userrepo;
	
	//insert a schedule assignment
	//insert a schedule assignment
		public ResponseEntity<?> insertScheduleAssignment(ScheduleAssignmentEntity sa) {
			TeacherScheduleEntity ts = tsrepo.findById(sa.getTeacherSchedule().getTeacherScheduleId()).orElse(null);
			UserEntity req = userrepo.findById(sa.getRequester().getUid()).orElse(null);
			UserEntity aprvr = userrepo.findById(sa.getRequester().getUid()).orElse(null);
			if(ts == null) {
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.body("Teacher schedule with ID " + sa.getTeacherSchedule().getTeacherScheduleId() + " is not found");
			}
			ScheduleAssignmentEntity newsa = new ScheduleAssignmentEntity();
			newsa.setApprover(aprvr);
			newsa.setRemarks(sa.getRemarks());
			newsa.setRequester(req);
			newsa.setStatus(sa.getStatus());
			newsa.setTeacherSchedule(ts);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(sarepo.save(newsa));
		}
	
	//get all schedule assignment data
	public List<ScheduleAssignmentEntity> getAllScheduleAssignment(){
		return sarepo.findAll();
	}
}
