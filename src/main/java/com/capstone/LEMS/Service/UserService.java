package com.capstone.LEMS.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userrepo;
	
	//registration or create user
	public UserEntity UserRegister (UserEntity user) {
		return userrepo.save(user);
	}
	
	//login
	public String UserLogin (String idnum, String password) {
		UserEntity user = userrepo.findByIdnum(idnum);
		
		if(user != null && user.getPassword().equals(password)) {
			return "login successfully";
		}else {
			return "incorrect id number or password";
		}
	}
}
