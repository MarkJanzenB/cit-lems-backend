package com.capstone.LEMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.ManufacturerEntity;
import com.capstone.LEMS.Service.ManufacturerService;

@RestController
@RequestMapping("/manufacturer")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class ManufacturerController {
	@Autowired
	ManufacturerService mserv;
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAll(){
		return mserv.getAll();
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody ManufacturerEntity newManu){
		return mserv.add(newManu);
	}
}
