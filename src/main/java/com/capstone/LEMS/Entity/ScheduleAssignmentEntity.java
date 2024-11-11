package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Schedule_Assignment")
public class ScheduleAssignmentEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sched_assign_id")
	private int schedAssignId;
	private String status;
	private String remarks;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id")
	private UserEntity requester;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approver_id")
	private UserEntity approver;
	
	public ScheduleAssignmentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScheduleAssignmentEntity(int schedAssignId, String status, String remarks, UserEntity requester,
			UserEntity approver) {
		super();
		this.schedAssignId = schedAssignId;
		this.status = status;
		this.remarks = remarks;
		this.requester = requester;
		this.approver = approver;
	}



	@JsonProperty("sched_assign_id")
	public int getSchedAssignId() {
		return schedAssignId;
	}

	public void setSchedAssignId(int schedAssignId) {
		this.schedAssignId = schedAssignId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonProperty("requester_id")
	public UserEntity getRequester() {
		return requester;
	}

	public void setRequester(UserEntity requester) {
		this.requester = requester;
	}

	@JsonProperty("approver_id")
	public UserEntity getApprover() {
		return approver;
	}

	public void setApprover(UserEntity approver) {
		this.approver = approver;
	}
	
	
}
