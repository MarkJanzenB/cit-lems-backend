package com.capstone.LEMS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ManufacturerEntity;
import com.capstone.LEMS.Repository.ManufacturerRepository;

@Service
public class ManufacturerService {
	@Autowired
	ManufacturerRepository mrepo;
	
	public ResponseEntity<?> getAll(){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(mrepo.findAll());
	}
	
	public ResponseEntity<?> add(ManufacturerEntity newManu){
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(mrepo.save(newManu));
	}
}
