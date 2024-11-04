package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Item")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "group_id")
    private int groupId;
    @Column(name = "unique_id")
    private double uniqueId;

    public ItemEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ItemEntity(int itemId, String itemName, String categoryId, int groupId, double uniqueId) {
        super();
        this.itemId = itemId;
        this.itemName = itemName;
        this.categoryId = categoryId;
        this.groupId = groupId;
        this.uniqueId = uniqueId;
    }

    @JsonProperty("item_id")
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

    @JsonProperty("category_id")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("group_id")
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @JsonProperty("unique_id")
    public double getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(double uniqueId) {
        this.uniqueId = uniqueId;
    }
}
