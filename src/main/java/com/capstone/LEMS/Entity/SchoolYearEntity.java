package com.capstone.LEMS.Entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "SchoolYear")
public class SchoolYearEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sy_id")
    private int syId;
    
    @Column(name = "year", nullable = false)
    private String year;
    
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    
    private String instiId; // Added field for institution ID
    
    // Default constructor
    public SchoolYearEntity() {
    }
    
    // Constructor with fields
    public SchoolYearEntity(String year, Date startDate, Date endDate, String instiId) {
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.instiId = instiId;
    }
    
    // Getters and setters
    public int getSyId() {
        return syId;
    }
    
    public void setSyId(int syId) {
        this.syId = syId;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getInstiId() {
        return instiId;
    }
    
    public void setInstiId(String instiId) {
        this.instiId = instiId;
    }
    
    @Override
    public String toString() {
        return "SchoolYearEntity{" +
                "syId=" + syId +
                ", year='" + year + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", instiId='" + instiId + '\'' +
                '}';
    }
}