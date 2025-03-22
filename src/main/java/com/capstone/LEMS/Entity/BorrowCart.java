package com.capstone.LEMS.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "borrow_cart")
public class BorrowCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String instiId;

    @Column(nullable = false)
    private int itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private int quantity;
    
	@OneToMany(mappedBy = "borrowCart", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
	@JsonIgnore
	private List<ItemEntity> items = new ArrayList<>();

    // Default constructor
    public BorrowCart() {
    }

    // Constructor
    public BorrowCart(String instiId, int itemId, String itemName, String categoryName, int quantity) {
        this.instiId = instiId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstiId() {
        return instiId;
    }

    public void setInstiId(String instiId) {
        this.instiId = instiId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}