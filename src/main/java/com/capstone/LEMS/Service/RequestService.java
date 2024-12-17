package com.capstone.LEMS.Service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.RequestEntity;
import com.capstone.LEMS.Repository.RequestRepository;

@Service
public class RequestService {
	@Autowired
	RequestRepository reqrepo;
	
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
}
