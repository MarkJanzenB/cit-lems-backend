package com.capstone.LEMS.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.UserEntity;
import com.capstone.LEMS.Repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	AuthenticationManager authmanager;
	
	@Autowired
	JwtService jwtserv;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public UserEntity UserRegister (UserEntity user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setNew(true);
		return userrepo.save(user);
	}
	
	public String verify (String insti_id, String password) {
		UserEntity user = userrepo.findByInstiId(insti_id);
		
		if(user == null) {
			return "User doesn't exists";
		}
		
		try {
			Authentication auth = 
					authmanager
						.authenticate(new UsernamePasswordAuthenticationToken(
								insti_id, 
								password));
			if(auth.isAuthenticated()) {
				return jwtserv.generateToken(insti_id, user.getRole().getRoleId(), user.getFname());
			}
		} catch (Exception e) {
			return "Incorrect Password";
		}
		
		return "Incorrect Password";
	}
	
	public List<UserEntity> getAllUsers(){
		return userrepo.findAll();
	}
	
	public String notNew(String instiId) {
		UserEntity user = userrepo.findByInstiId(instiId);
		
		if(user == null) {
			return instiId;
		}
		
		user.setNew(false);
		userrepo.save(user);
		
		return "User is now not new";
	}
	
	public boolean isUserNew(String instiId) {
		UserEntity user = userrepo.findByInstiId(instiId);
		
		return user.isNew();
	}
}
