package com.capstone.LEMS.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime; // Import LocalDateTime

@Entity
@Table(name = "preparing_item")
public class PreparingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false, length = 255) // Added length constraint
    private String referenceCode;

    @Column(name = "unique_id", length = 255) // Added length constraint
    private String uniqueId;

    @Column(nullable = false, length = 255) // Added length constraint
    private String instiId;

    @Column(nullable = false, length = 255) // Added length constraint
    private String itemName;

    @Column(nullable = false, length = 255) // Added length constraint
    private String categoryName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, length = 255) // Added length constraint
    private String status;

    @Column(name = "date_created") // Added date_created field
    private LocalDate dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;


    // Default constructor
    public PreparingItemEntity() {}

    // Parameterized constructor (excluding id and uniqueId)
    public PreparingItemEntity(String referenceCode, String instiId, String itemName, String categoryName, int quantity, String status, LocalDate dateCreated, ItemEntity item) {
        this.referenceCode = referenceCode;
        this.instiId = instiId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.status = status;
        this.dateCreated = dateCreated;
        this.item = item;

    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getInstiId() {
        return instiId;
    }

    public void setInstiId(String instiId) {
        this.instiId = instiId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}