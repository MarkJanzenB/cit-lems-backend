package com.capstone.LEMS.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Reservation")
public class ReservationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="reservation_id")
	private int reservationId;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime reservationDate;
	
	public ReservationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReservationEntity(int reservationId, LocalDateTime reservationDate) {
		super();
		this.reservationId = reservationId;
		this.reservationDate = reservationDate;
	}

	@JsonProperty("reservation_id")
	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	@JsonProperty("reservation_date")
	public LocalDateTime getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDateTime reservationDate) {
		this.reservationDate = reservationDate;
	}
	
	
}
