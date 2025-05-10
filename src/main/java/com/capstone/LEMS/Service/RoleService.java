package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.RoleEntity;
import com.capstone.LEMS.Repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository rolerepo;
	
	//for adding roles
	public RoleEntity AddRole(RoleEntity role) {
		return rolerepo.save(role);
	}
	
	//get a list of all roles
	public List<RoleEntity> getAllRoles(){
		return rolerepo.findAll();
	}
}
