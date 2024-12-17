package com.capstone.LEMS.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="year_section")
public class YearSectionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int yearId;
	
	@Column(name="year_and_section")
	private String yearAndSection;

	public YearSectionEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public YearSectionEntity(int yearId, String yearAndSection) {
		super();
		this.yearId = yearId;
		this.yearAndSection = yearAndSection;
	}

	public int getYearId() {
		return yearId;
	}

	public void setYearId(int yearId) {
		this.yearId = yearId;
	}

	public String getYearAndSection() {
		return yearAndSection;
	}

	public void setYearAndSection(String yearAndSection) {
		this.yearAndSection = yearAndSection;
	}
}
