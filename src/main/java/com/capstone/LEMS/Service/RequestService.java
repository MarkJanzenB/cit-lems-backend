package com.capstone.LEMS.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.RequestEntity;
import com.capstone.LEMS.Entity.SubjectEntity;
import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.RequestRepository;
import com.capstone.LEMS.Repository.SubjectRepository;
import com.capstone.LEMS.Repository.UserRepository;

@Service
public class RequestService {
	@Autowired
	RequestRepository reqrepo;
	
	@Autowired
	UserRepository userrepo;
	
    @Autowired
    SubjectRepository srepo;
	
	private static final Logger log = LoggerFactory.getLogger(RequestService.class);
	
	public ResponseEntity<?> addRequest(RequestEntity request){
		log.info("Setting request date to: {}", LocalDateTime.now());
		request.setDateRequested(LocalDateTime.now());
		
		log.info("Adding request to database.");
		return ResponseEntity.ok(reqrepo.save(request));
	}
	
	public ResponseEntity<?> getRequests(){
		log.info("Getting a list of requests");
		return ResponseEntity.ok(reqrepo.findAll());
	}
	
	public ResponseEntity<?> updateRequest(int reqId, RequestEntity newReq){
		log.info("Running updateRequest in RequestService");
		
		log.info("fetching");
		Optional<RequestEntity> reqOptional = reqrepo.findById(reqId);
		
		if(reqOptional.isPresent()) {
			log.info("We hav found request with the Id of: {}", reqId);
			RequestEntity req = reqOptional.get();
			
			if(newReq.getTeacher() != null && newReq.getTeacher().getUid() != 0) {
				log.info("Updating teacher");
				log.info("Fetching teacher with id: {}", newReq.getTeacher().getUid());
				Optional<UserEntity> teachOptional = userrepo.findById(newReq.getTeacher().getUid());
			
				if(teachOptional.isPresent()) {
					log.info("Found teacher with id: {}", newReq.getTeacher().getUid());
					req.setTeacher(teachOptional.get());
				}else {
					log.warn("Not found teacher with id: {}", newReq.getTeacher().getUid());
					return ResponseEntity
							.status(HttpStatus.NOT_FOUND) //404
							.body("Teacher with ID " + newReq.getTeacher().getUid() + " is not found");
				}
			}
			
			if(newReq.getDateRequested() != null) {
				req.setDateRequested(newReq.getDateRequested());
			}
			
			if(newReq.getDateSchedule() != null) {
				req.setDateSchedule(newReq.getDateSchedule());
			}
			
			if(newReq.getStartTime() != null) {
				req.setStartTime(newReq.getStartTime());
			}
			
			if(newReq.getEndTime() != null) {
				req.setEndTime(newReq.getEndTime());
			}
			
			if(newReq.getApprover() != null && newReq.getApprover().getUid() != 0) {
				log.info("Updating approver");
				log.info("Fetching approver with id: {}", newReq.getApprover().getUid());
				Optional<UserEntity> approvOptional = userrepo.findById(newReq.getApprover().getUid());
			
				if(approvOptional.isPresent()) {
					log.info("Found approver with id: {}", newReq.getApprover().getUid());
					req.setApprover(approvOptional.get());
				}else {
					log.warn("Not found approver with id: {}", newReq.getApprover().getUid());
					return ResponseEntity
							.status(HttpStatus.NOT_FOUND) //404
							.body("Approver with ID " + newReq.getApprover().getUid() + " is not found");
				}
			}
			
			if(newReq.getDateApproved() != null) {
				req.setDateApproved(newReq.getDateApproved());
			}
			
			if(newReq.getRemarks() != null) {
				req.setRemarks(newReq.getRemarks());
			}
			
			if(newReq.getStatus() != null) {
				req.setStatus(newReq.getStatus());
			}
			
			if(newReq.getSubject() != null && newReq.getSubject().getSubjectId() != 0) {
				log.info("Updating subject");
				log.info("Fetching subject with id: {}", newReq.getSubject().getSubjectId());
				Optional<SubjectEntity> subOptional = srepo.findById(newReq.getSubject().getSubjectId());
			
				if(subOptional.isPresent()) {
					log.info("Found subject with id: {}", newReq.getSubject().getSubjectId());
					req.setSubject(subOptional.get());
				}else {
					log.warn("Not found subject with id: {}", newReq.getSubject().getSubjectId());
					return ResponseEntity
							.status(HttpStatus.NOT_FOUND) //404
							.body("Subject with ID " + newReq.getSubject().getSubjectId() + " is not found");
				}
			}
			
			if(newReq.getRoom() != null) {
				req.setRoom(newReq.getRoom());
			}
			
			return ResponseEntity.ok(reqrepo.save(req));
		}else {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Something went wrong. Please check the request details");
		}
		
	}
}
