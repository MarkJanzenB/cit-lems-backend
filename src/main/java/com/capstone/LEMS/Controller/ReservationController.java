package com.capstone.LEMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.ReservationEntity;
import com.capstone.LEMS.Service.ReservationService;

@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class ReservationController {
	@Autowired
	ReservationService resserv;
	
	//for testing purposes
	@GetMapping("/message")
	public String message() {
		return "ReservationController is working";
	}
	
	//for adding reservation
	@PostMapping("/addreservation")
	public ReservationEntity addReservation(@RequestBody ReservationEntity reservation) {
		return resserv.addReservation(reservation);
	}
	
	//for getting all reservation data
	@GetMapping("/getallreservation")
	public List<ReservationEntity> getAllReservation(){
		return resserv.getAllReservation();
	}
}
