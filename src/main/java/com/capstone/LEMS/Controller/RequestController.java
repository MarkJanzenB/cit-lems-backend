package com.capstone.LEMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.RequestEntity;
import com.capstone.LEMS.Service.RequestService;

@RestController
@RequestMapping("/request")
@CrossOrigin
public class RequestController {
	@Autowired
	RequestService reqserv;
	
	@PostMapping("/addrequest")
	public ResponseEntity<?> addRequest(@RequestBody RequestEntity request){
		return reqserv.addRequest(request);
	}
	
	@GetMapping("/getrequests")
	public ResponseEntity<?> getRequests(){
		return reqserv.getRequests();
	}
	
	@PutMapping("/updaterequest")
	public ResponseEntity<?> updateRequest(@RequestParam int reqId, @RequestBody RequestEntity req){
		return reqserv.updateRequest(reqId, req);
	}
	@GetMapping("/getrequestbystatus")
	public ResponseEntity<?> getRequestByStatus(@RequestParam String status){
		return reqserv.getRequestByStatus(status);
	}
}
