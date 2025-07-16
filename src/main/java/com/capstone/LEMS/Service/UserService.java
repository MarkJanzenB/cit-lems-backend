package com.capstone.LEMS.Service;

import java.util.List;
import java.util.Map;
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
import com.capstone.LEMS.Entity.RoleEntity;
import com.capstone.LEMS.Repository.UserRepository;
import com.capstone.LEMS.Repository.RoleRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	RoleRepository rolerepo;
	
	@Autowired
	AuthenticationManager authmanager;
	
	@Autowired
	JwtService jwtserv;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	public UserEntity UserRegister (UserEntity user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user.setNew(true);
		UserEntity savedUser = userrepo.save(user);

		// If the user is registering as Lab In-Charge (role_id: 3), automatically create a teacher account
		if (user.getRole().getRoleId() == 3) {
			log.info("Creating teacher account for lab in-charge: {}", user.getInstiId());

			// Create a new user with teacher role
			UserEntity teacherUser = new UserEntity();
			teacherUser.setFname(user.getFname());
			teacherUser.setLname(user.getLname());
			teacherUser.setInstiId(user.getInstiId() + "-1"); // Append "-1" to create unique insti ID
			teacherUser.setEmail(user.getEmail());
			teacherUser.setPassword(user.getPassword()); // Already encoded above
			teacherUser.setNew(true);

			// Find and set the teacher role (role_id: 1)
			Optional<RoleEntity> teacherRole = rolerepo.findById(1);
			if (teacherRole.isPresent()) {
				teacherUser.setRole(teacherRole.get());
				try {
					userrepo.save(teacherUser);
					log.info("Teacher account successfully created for user: {} with modified ID: {}", user.getInstiId(), teacherUser.getInstiId());
				} catch (Exception e) {
					log.error("Error creating teacher account: ", e);
				}
			}
		}

		return savedUser;
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
				return jwtserv.generateToken(insti_id, user.getRole().getRoleId(), user.getFname(), user.getLname(), user.getUid());
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
	
	public ResponseEntity<?> getAllUsersByRoleId(int roleId){
		log.info("Fetching users with role Id: {}", roleId);
		List<UserEntity> users = userrepo.findByRole_RoleId(roleId);
		
		if(users.isEmpty()) {
			log.warn("There are no users with role: {}", roleId);
			return ResponseEntity
					.status(HttpStatus.NO_CONTENT)
					.body("Users with role " + roleId + " does not exist");
		}
		
		return ResponseEntity.ok(users);
	}
	
	public ResponseEntity<?> updateUser(UserEntity newUserDetails){
		log.info("Fetching user details for ID: {}", newUserDetails.getUid());
		Optional<UserEntity> user = userrepo.findById(newUserDetails.getUid());
		
		if(user.isEmpty()) {
			log.warn("User not found for ID: {}", newUserDetails.getUid());
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("User not found");
		}
		log.info("user with ID {} is found", newUserDetails.getUid() );
		
		UserEntity foundUser = user.orElseThrow();
		log.info("converting user: {}", newUserDetails.getUid() );
		
		if((foundUser.getInstiId().equals(newUserDetails.getInstiId()))) {
			log.info("updating user: {}", newUserDetails.getUid());
			foundUser.setFname(newUserDetails.getFname());
			foundUser.setLname(newUserDetails.getLname());
			foundUser.setEmail(newUserDetails.getEmail());
		}else {
			UserEntity conflictingUser = userrepo.findByInstiId(newUserDetails.getInstiId());
			if(conflictingUser != null) {
				log.info("insti id conflict with user ID: {}", newUserDetails.getUid());
				return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body("Institute ID already exists");
			}else {
				log.info("updating user w/ insti ID for user: {}", newUserDetails.getUid());
				foundUser.setFname(newUserDetails.getFname());
				foundUser.setLname(newUserDetails.getLname());
				foundUser.setEmail(newUserDetails.getEmail());
				foundUser.setInstiId(newUserDetails.getInstiId());
			}
		}
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(userrepo.save(foundUser));
	}
	
	public ResponseEntity<?> editPfp(Map<String, Object> newPfpDetails){
		String newPfp = (String) newPfpDetails.get("pfp_url");
		int uid = (int) newPfpDetails.get("uid");
		UserEntity user = userrepo.findById(uid).orElse(null);
		
		if(user == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("user " + uid + " not found");
		}
		
		user.setPfp(newPfp);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(userrepo.save(user));
	}
	
	public ResponseEntity<?> activateTeacherRole(UserEntity teacherUser) {
		log.info("Activating teacher role for user with insti_id: {}", teacherUser.getInstiId());
		
		// Check if the lab in-charge exists
		UserEntity labInCharge = userrepo.findByInstiId(teacherUser.getInstiId());
		if (labInCharge == null) {
			log.warn("Lab in-charge account not found for insti_id: {}", teacherUser.getInstiId());
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Lab in-charge account not found");
		}
		
		// Generate the modified insti ID for teacher role
		String teacherInstiId = teacherUser.getInstiId() + "-1";

		// Check if user already has a teacher account with the modified ID
		UserEntity existingTeacher = userrepo.findByInstiId(teacherInstiId);
		if (existingTeacher != null) {
			log.info("User already has a teacher account with ID: {}", teacherInstiId);
			return ResponseEntity
					.status(HttpStatus.CONFLICT)
					.body("User already has a teacher account");
		}
		
		// Create a new user with teacher role
		UserEntity newTeacher = new UserEntity();
		newTeacher.setFname(teacherUser.getFname());
		newTeacher.setLname(teacherUser.getLname());
		newTeacher.setInstiId(teacherInstiId); // Use the modified insti ID
		newTeacher.setEmail(teacherUser.getEmail());
		newTeacher.setPassword(encoder.encode(teacherUser.getPassword()));
		newTeacher.setNew(true);
		
		// Find and set the teacher role (role_id: 1)
		Optional<RoleEntity> teacherRole = rolerepo.findById(1);
		if (teacherRole.isEmpty()) {
			log.error("Teacher role (role_id: 1) not found in the database");
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Teacher role not found in the system");
		}
		
		newTeacher.setRole(teacherRole.get());
		
		// Save the new teacher account
		try {
			userrepo.save(newTeacher);
			log.info("Teacher account successfully created with modified ID: {}", teacherInstiId);
			return ResponseEntity.ok("Teacher account successfully activated with ID: " + teacherInstiId);
		} catch (Exception e) {
			log.error("Error creating teacher account: ", e);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error activating teacher account: " + e.getMessage());
		}
	}
}