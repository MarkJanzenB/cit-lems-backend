package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "borrow_item")
public class BorrowItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int instiId;

    @Column(nullable = false)
    private int itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String borrower;

    // Default constructor
    public BorrowItem() {
    }

    // Constructor
    public BorrowItem(int instiId, int itemId, String itemName, String categoryName, int quantity, String borrower) {
        this.instiId = instiId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.borrower = borrower;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("insti_id")
    public int getInstiId() {
        return instiId;
    }

    public void setInstiId(int instiId) {
        this.instiId = instiId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @JsonProperty("category_name")
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

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}