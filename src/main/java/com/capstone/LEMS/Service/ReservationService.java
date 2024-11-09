package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.ReservationEntity;
import com.capstone.LEMS.Repository.ReservationRepository;

@Service
public class ReservationService {
	@Autowired
	ReservationRepository resrepo;
	
	public ReservationEntity addReservation(ReservationEntity reservation) {
		return resrepo.save(reservation);
	}
	
	public List<ReservationEntity> getAllReservation(){
		return resrepo.findAll();
	}
}
