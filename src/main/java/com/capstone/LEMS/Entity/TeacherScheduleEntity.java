package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Time;

import java.util.Date;

@Entity
@Table(name = "TeacherSchedule")
public class TeacherScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="teacher_schedule_id")
    private int teacherScheduleId;
    @Column(name="sched_assign_id")
    private int schedAssignId;
    @Column(name = "subject_id")
    private int subjectId;
    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;
    @Column(name="lab_num")
    private int labNum;
    @Column(name = "date")
    private Date date;
    @Column(name = "teacher_id")
    private int teacherId;

    public TeacherScheduleEntity() {
        super();
    }
    public TeacherScheduleEntity(int teacherScheduleId,int schedAssignId,int subjectId,Time startTime,Time endTime,int labNum,Date date,int teacherId) {
        super();
        this.teacherScheduleId = teacherScheduleId;
        this.schedAssignId = schedAssignId;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.labNum = labNum;
        this.date = date;
        this.teacherId = teacherId;
    }


    @JsonProperty("teacher_schedule_id")
    public int getTeacherScheduleId() {
        return teacherScheduleId;
    }
    public void setTeacherScheduleId() {
        this.teacherScheduleId = teacherScheduleId;
    }
    @JsonProperty("sched_assign_id")
    public int getSchedAssignId() {
        return schedAssignId;
    }
    public void setSchedAssignId(int schedAssignId) {
        this.schedAssignId = schedAssignId;
    }
    @JsonProperty("subject_id")
    public int getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
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

}
