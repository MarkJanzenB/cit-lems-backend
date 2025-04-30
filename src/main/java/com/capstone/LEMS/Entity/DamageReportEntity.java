package com.capstone.LEMS.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "damage_report")
public class DamageReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "date_borrowed")
    private LocalDate dateBorrowed;

    private String subject;

//    @Column(name = "year_sec")
//    private String yearSec;
    
    @ManyToOne
    @JoinColumn(name = "year_sec", nullable = true)
    private YearSectionEntity yearSec;

    private String instructor;

    private String photo;

    private String accountable;

    @Column(name = "incident_status")
    private String incidentStatus;

    // Getters and Setters
    public Long getReportId() {
        return report_id;
    }

    public void setReportId(Long report_id) {
        this.report_id = report_id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAccountable() {
        return accountable;
    }

    public void setAccountable(String accountable) {
        this.accountable = accountable;
    }

    public String getIncidentStatus() {
        return incidentStatus;
    }

    public void setIncidentStatus(String status) {
        this.incidentStatus = status;
    }

	public YearSectionEntity getYearSec() {
		return yearSec;
	}

	public void setYearSec(YearSectionEntity yearSec) {
		this.yearSec = yearSec;
	}
    
    
}