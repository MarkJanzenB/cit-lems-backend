package com.capstone.LEMS.Entity;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name="subject")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subject_id")
	private int subject_id;
    @Column(name = "subject_name")
    private String subjectName;
    
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RequestEntity> requests = new ArrayList<>();
	@OneToMany(mappedBy = "subject")
	@JsonIgnore
	private List<TeacherScheduleEntity> teacherSchedules = new ArrayList<>();


	public SubjectEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubjectEntity(int subject_id, String subjectName) {
		super();
		this.subject_id = subject_id;
		this.subjectName = subjectName;
	}

	public int getSubjectId() {
		return subject_id;
	}

	@JsonProperty("subject_id")
	public void setSubjectId(int subject_id) {
		this.subject_id = subject_id;
	}

	@JsonProperty("subject_name")
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public List<TeacherScheduleEntity> getTeacherSchedules() {
		return teacherSchedules;
	}

	public void setTeacherSchedules(List<TeacherScheduleEntity> teacherSchedules) {
		this.teacherSchedules = teacherSchedules;
	}

}
