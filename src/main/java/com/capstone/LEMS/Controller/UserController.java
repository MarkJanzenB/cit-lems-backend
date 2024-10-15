package com.capstone.LEMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userserv;
	
	//for testing purposes if Springboot is working
	@GetMapping("/message")
	public String testMessage() {
		return "Looking, Good!";
	}
	
	//register mapping
	@PostMapping("/register")
	public UserEntity UserRegister(@RequestBody UserEntity user) {
		return userserv.UserRegister(user);
	}
	
	
	@GetMapping("/login")
	public String UserLogin(@RequestParam String idnum, @RequestParam String password) {
		return userserv.UserLogin(idnum, password);
	}
}
