package com.capstone.LEMS.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "borrow_items")
public class BorrowItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String borrowedId; // Unique Borrowed ID per transaction

    @ManyToOne
    @JoinColumn(name = "uid",  nullable = false)
    private UserEntity user;

    private Long itemId;
    private String itemName;
    private String categoryName;
    private int quantity;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowedDate;
    
	@OneToMany(mappedBy = "borrowItem", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
	@JsonIgnore
	private List<ItemEntity> items = new ArrayList<>();

    public BorrowItem() {}

    public BorrowItem(String borrowedId, Long itemId, String itemName, String categoryName, int quantity, String status, Date borrowedDate, UserEntity user) {
        this.borrowedId = borrowedId;
//        this.instiId = instiId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.status = status;
        this.borrowedDate = borrowedDate;
        this.user = user;
    }

    // ✅ Getters and Setters
    public String getBorrowedId() { return borrowedId; }
    public void setBorrowedId(String borrowedId) { this.borrowedId = borrowedId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

//    public String getInstiId() { return instiId; }
//    public void setInstiId(String instiId) { this.instiId = instiId; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(Date borrowedDate) { this.borrowedDate = borrowedDate; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}
