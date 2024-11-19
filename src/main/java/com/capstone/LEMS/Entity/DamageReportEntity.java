package com.capstone.LEMS.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "damage_report")
public class DamageReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long report_id;

    @Column(name = "material_id")
    private String materialId;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "date_borrowed")
    private LocalDate dateBorrowed;

    private int qty;

    @Column(name = "group_number")
    private String group;

    @Column(name = "year_sec")
    private String yearSec;

    private String instructor;

    private String photo;

    private String accountable;

    private String status;

    // Getters and Setters
    public Long getReportId() {
        return report_id;
    }

    public void setReportId(Long report_id) {
        this.report_id = report_id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public LocalDate getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(LocalDate dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getYearSec() {
        return yearSec;
    }

    public void setYearSec(String yearSec) {
        this.yearSec = yearSec;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}