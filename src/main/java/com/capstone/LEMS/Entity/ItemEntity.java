package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    @Column(name = "unique_id")
    private double uniqueId;
    
    @ManyToOne
    @JoinColumn(name="category_id")
    private ItemCategoryEntity itemCategory;
    
    @OneToOne
    @JoinColumn(name="group_id")
    private GroupEntity group;

    public ItemEntity() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	public ItemEntity(int itemId, String itemName, double uniqueId, ItemCategoryEntity itemCategory,
			GroupEntity group) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.uniqueId = uniqueId;
		this.itemCategory = itemCategory;
		this.group = group;
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

    @JsonProperty("unique_id")
    public double getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(double uniqueId) {
        this.uniqueId = uniqueId;
    }

    @JsonProperty("category_id")
	public ItemCategoryEntity getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategoryEntity itemCategory) {
		this.itemCategory = itemCategory;
	}

	@JsonProperty("group_id")
	public GroupEntity getGroup() {
		return group;
	}

	public void setGroup(GroupEntity group) {
		this.group = group;
	}
}
