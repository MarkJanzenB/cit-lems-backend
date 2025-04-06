package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Time;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.Date;




//to be revised
@Entity
@Table(name = "TeacherSchedule")
public class TeacherScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_schedule_id")
    private int teacherScheduleId;
    //Time default format is HH:mm:ss
    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;
    @Column(name="lab_num")
    private int labNum;
    @Column(name = "date")
    //Date default format is YYYY-MM-DD
    private Date date;
    @Column(name = "teacher_id")
    private int teacherId;
    @ManyToOne
    @JoinColumn(name = "year_id")
    private YearSectionEntity yearSection;

    @OneToOne(mappedBy = "teacherSchedule")
	private BorrowItemEntity borrowItemEntity;

    //Add sched_assign_id FK
    //Add subject_id FK

    public TeacherScheduleEntity() {
        super();
    }
    
    public TeacherScheduleEntity(int teacherScheduleId, Time startTime, Time endTime, int labNum, Date date,
			int teacherId, YearSectionEntity yearSection) {
		super();
		this.teacherScheduleId = teacherScheduleId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.labNum = labNum;
		this.date = date;
		this.teacherId = teacherId;
        this.yearSection = new YearSectionEntity();
	}

	@JsonProperty("teacher_schedule_id")
    public int getTeacherScheduleId() {
        return teacherScheduleId;
    }
    public void setTeacherScheduleId(int teacherScheduleId) {
        this.teacherScheduleId = teacherScheduleId;
    }
    @JsonProperty("start_time")
    public Time getStartTime() {
        return startTime;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    @JsonProperty("end_time")
    public Time getEndTime() {
        return endTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
    @JsonProperty("lab_num")
    public int getLabNum() {
        return labNum;
    }
    public void setLabNum(int labNum) {
        this.labNum = labNum;
    }
    @JsonProperty("date")
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @JsonProperty("teacher_id")
    public int getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    @JsonProperty("year_section")
    public YearSectionEntity getYearSection() {
        return yearSection;
    }

    public void setYearSection(YearSectionEntity yearSection) {
        this.yearSection = yearSection;
    }
}
