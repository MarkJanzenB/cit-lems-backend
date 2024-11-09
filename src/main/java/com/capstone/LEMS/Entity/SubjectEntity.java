package com.capstone.LEMS.Entity;


import jakarta.persistence.*;

@Entity
@Table(name="Subject")
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subject_id;
    @Column(name = "subject_name")
    private String subject_name;
    @Column(name = "year_section")
    private String year_section;

    public SubjectEntity(){

    }
    public SubjectEntity(int subject_id, String subject_name, String year_section){
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.year_section = year_section;
    }

    public void setSubjectId(int subject_id){
        this.subject_id = subject_id;
    }
    public int getSubjectId(){
        return subject_id;
    }
    public void setSubjectName(String subject_name){
        this.subject_name = subject_name;
    }
    public String getSubjectName(){
        return subject_name;
    }
    public void setYearSection(String year_section){
        this.year_section = year_section;
    }
    public String getYearSection(){
        return year_section;
    }



}
