package com.capstone.LEMS.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	@Autowired
	UserService userserv;
	
	@GetMapping("/message")
	public String testMessage() {
		return "UserController is working";
	}
	
	@PostMapping("/register")
	public UserEntity UserRegister(@RequestBody UserEntity user) {
		return userserv.UserRegister(user);
	}
	
	@PostMapping("/login")
	public String UserLogin(@RequestBody Map<String, String> loginDetails) {
	    String insti_id = loginDetails.get("insti_id");
	    String password = loginDetails.get("password");
	    return userserv.verify(insti_id, password);
	}

	
	@GetMapping("/getallusers")
	public List<UserEntity> getAllUser(){
		return userserv.getAllUsers();
	}
}
