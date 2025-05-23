package com.capstone.LEMS.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "Item")
public class ItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private int itemId;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "unique_id")
	private String uniqueId;

	@Column(name = "is_auto_uid")
	private boolean isAutoUid;

	private String status;

	@Column(name = "date_added")
	private LocalDate dateAdded;

	private int quantity;

	@Column(name = "expiry_date")
	private LocalDate expiryDate;

	@Column(nullable = false)
	private String variant;

	@ManyToOne
	@JoinColumn(name = "inventory_id", nullable = true)
	private InventoryEntity inventory;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "borrowcart_id", nullable = true)
	private BorrowCartEntity borrowCart;

	@ManyToOne
	@JoinColumn(name = "preparing_item_id", nullable = true)
	private PreparingItemEntity preparingItem;

	@ManyToOne
	@JoinColumn(name = "manufacturer_id", nullable = true)
	private ManufacturerEntity manufacturer;

	@ManyToOne
	@JoinColumn(name = "resupply_id", nullable = true)
	private BatchResupplyEntity batchResupply;

	public ItemEntity() {
		super();
	}

	public ItemEntity(int itemId, String itemName, String uniqueId, InventoryEntity inventory, UserEntity user, String status, BorrowCartEntity borrowCart, PreparingItemEntity preparingItem, int quantity, LocalDate expiryDate, String variant, ManufacturerEntity manufacturer, BatchResupplyEntity batchResupply) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.uniqueId = uniqueId;
		this.inventory = inventory;
		this.isAutoUid = false;
		this.user = user;
		this.status = status;
		this.borrowCart = borrowCart;
		this.preparingItem = preparingItem;
		this.quantity = quantity;
		this.expiryDate = expiryDate;
		this.variant = variant;
		this.manufacturer = manufacturer;
		this.batchResupply = batchResupply;
	}

	@PrePersist
	protected void onCreate() {
		this.dateAdded = LocalDate.now();
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
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public boolean isAutoUid() {
		return isAutoUid;
	}

	public void setAutoUid(boolean isAutoUid) {
		this.isAutoUid = isAutoUid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("date_added")
	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public InventoryEntity getInventory() {
		return inventory;
	}

	public void setInventory(InventoryEntity inventory) {
		this.inventory = inventory;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public BorrowCartEntity getBorrowCart() {
		return borrowCart;
	}

	public void setBorrowCart(BorrowCartEntity borrowCart) {
		this.borrowCart = borrowCart;
	}
	
	public PreparingItemEntity getPreparingItem() {
		return preparingItem;
	}

	public void setPreparingItem(PreparingItemEntity preparingItem) {
		this.preparingItem = preparingItem;
	}

	public ManufacturerEntity getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerEntity manufacturer) {
		this.manufacturer = manufacturer;
	}

	public BatchResupplyEntity getBatchResupply() {
		return batchResupply;
	}

	public void setBatchResupply(BatchResupplyEntity batchResupply) {
		this.batchResupply = batchResupply;
	}
}