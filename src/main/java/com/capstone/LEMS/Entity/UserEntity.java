package com.capstone.LEMS.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tblusers")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int uid;
	
	private String fname;
	private String lname;
	private String password;
	private String idnum;
	private String email;
	private String acctype;
	
	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserEntity(int uid, String fname, String lname, String password, String idnum, String email, String acctype) {
		super();
		this.uid = uid;
		this.fname = fname;
		this.lname = lname;
		this.password = password;
		this.idnum = idnum;
		this.email = email;
		this.acctype = acctype;
	}


	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}
	
	
}
