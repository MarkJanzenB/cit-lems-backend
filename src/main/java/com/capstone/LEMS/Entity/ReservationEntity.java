package com.capstone.LEMS.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserEntity user;
	
    @ManyToMany
    @JoinTable(
        name = "reservation_items",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemEntity> item;
	
	//add teacher schedule entity
	
	public ReservationEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReservationEntity(int reservationId, LocalDateTime reservationDate, UserEntity user, List<ItemEntity> item) {
		super();
		this.reservationId = reservationId;
		this.reservationDate = reservationDate;
		this.user = user;
		this.item = item;
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

	@JsonProperty("user_id")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@JsonProperty("get_item")
	public List<ItemEntity> getItem() {
		return item;
	}

	public void setItem(List<ItemEntity> item) {
		this.item = item;
	}
	
	
}
