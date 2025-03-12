package com.capstone.LEMS.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class StudentEntity {
    @Id
    @GeneratedValue
    private int student_id;
    @Column(name = "school_id")
    private String school_id;
    @Column(name = "student_name")
    private String student_name;

    public StudentEntity(){}

    public StudentEntity(int student_id, String school_id, String student_name){
        this.student_id = student_id;
        this.school_id = school_id;
        this.student_name = student_name;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
