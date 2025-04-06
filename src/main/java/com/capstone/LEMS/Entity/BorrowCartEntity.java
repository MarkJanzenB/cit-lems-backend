package com.capstone.LEMS.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "borrow_cart")
public class BorrowCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String instiId;

    @Column(nullable = true) // Nullable since itemId is assigned only after finalization
    private Integer itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String categoryName;

    @Column(nullable = false)
    private int quantity;

    // Default constructor
    public BorrowCartEntity() {
    }

    // Constructor without itemId (for unfinalized items)
    public BorrowCartEntity(String instiId, String itemName, String categoryName, int quantity) {
        this.instiId = instiId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
    }

    // Constructor with itemId (optional, when itemId is assigned later)
    public BorrowCartEntity(String instiId, Integer itemId, String itemName, String categoryName, int quantity) {
        this.instiId = instiId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
    }

    // Getters and Setters
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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
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
