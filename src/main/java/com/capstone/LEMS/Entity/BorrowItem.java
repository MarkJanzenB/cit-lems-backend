package com.capstone.LEMS.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "borrow_items")
public class BorrowItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instiId;
    private Long itemId;
    private String itemName;
    private String categoryName;
    private int quantity;
    private String status;

    // ✅ Add this constructor
    public BorrowItem(String instiId, Long itemId, String itemName, String categoryName, int quantity, String status) {
        this.instiId = instiId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.status = status;
    }

    // ✅ Add a no-args constructor (required by JPA)
    public BorrowItem() {}

    // ✅ Add getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInstiId() { return instiId; }
    public void setInstiId(String instiId) { this.instiId = instiId; }

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
}
