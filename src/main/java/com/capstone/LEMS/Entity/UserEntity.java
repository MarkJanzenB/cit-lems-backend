package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String password;
	@Column(name = "insti_id")
	private String instiId;
	private String email;
	
	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserEntity(int userId, String firstName, String lastName, String password, String instiId, String email) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.instiId = instiId;
		this.email = email;
	}

	@JsonProperty("user_id")
	public int getUid() {
		return userId;
	}

	public void setUid(int userId) {
		this.userId = userId;
	}

	@JsonProperty("first_name")
	public String getFname() {
		return firstName;
	}

	public void setFname(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("last_name")
	public String getLname() {
		return lastName;
	}

	public void setLname(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty("insti_id")
	public String getIdnum() {
		return instiId;
	}

	public void setIdnum(String instiId) {
		this.instiId = instiId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
