package com.capstone.LEMS.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
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
				return jwtserv.generateToken(insti_id, user.getRole().getRoleId(), user.getFname(), user.getUid());
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
	
	public boolean isUserAlrdyExists(String instiId) {
		UserEntity user = userrepo.findByInstiId(instiId);
		
		if(user == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public ResponseEntity<?> getUserDetails(int uid){
		log.info("Fetching user details for ID: {}", uid);
		
		Optional<UserEntity> user = userrepo.findById(uid);
		
		if(user.isEmpty()) {
			log.warn("User not found for ID: {}", uid);
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("User not found");
		}
		
		log.info("User found: {}", user.get());
		return ResponseEntity.ok(user.get());
	}
}
