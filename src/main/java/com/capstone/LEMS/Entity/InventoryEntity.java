package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Inventory")
public class InventoryEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private int inventoryId;
    @Column(name="item_id")
    private int itemId;
    @Column(name = "quantity")
    private float quantity;
    @Column(name = "unit")
    private String unit;

    public InventoryEntity() {
        super();

    }
    public InventoryEntity(int inventoryId, int itemId, int quantity, String unit) {
        super();
        this.inventoryId = inventoryId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unit = unit;
    }

    @JsonProperty("inventory_id")
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    @JsonProperty("item_id")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @JsonProperty("quantity")
    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {this.quantity = quantity; }

    @JsonProperty("unit")
    public String  getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
