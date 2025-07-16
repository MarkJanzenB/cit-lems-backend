// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Entity/BatchPulloutEntity.java
package com.capstone.LEMS.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BatchPullout")
public class BatchPulloutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pullout_id")
    private int pulloutId;

    @Column(name = "date_pullout", nullable = false)
    private LocalDate datePullout;

    @ManyToOne
    @JoinColumn(name = "pulled_by", nullable = false)
    private UserEntity pulledBy;

    @OneToMany(mappedBy = "batchPullout", cascade = CascadeType.ALL)
    @JsonIgnore // Avoid circular serialization
    private List<ItemEntity> items = new ArrayList<>();

    public BatchPulloutEntity() {
    }

    public BatchPulloutEntity(LocalDate datePullout, UserEntity pulledBy) {
        this.datePullout = datePullout;
        this.pulledBy = pulledBy;
    }

    // Getters and Setters
    public int getPulloutId() {
        return pulloutId;
    }

    public void setPulloutId(int pulloutId) {
        this.pulloutId = pulloutId;
    }

    public LocalDate getDatePullout() {
        return datePullout;
    }

    public void setDatePullout(LocalDate datePullout) {
        this.datePullout = datePullout;
    }

    public UserEntity getPulledBy() {
        return pulledBy;
    }

    public void setPulledBy(UserEntity pulledBy) {
        this.pulledBy = pulledBy;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    // Helper method to add an item to the pullout batch
    public void addItem(ItemEntity item) {
        this.items.add(item);
        item.setBatchPullout(this); // Set the back-reference
    }

    // Helper method to remove an item from the pullout batch
    public void removeItem(ItemEntity item) {
        this.items.remove(item);
        item.setBatchPullout(null); // Remove the back-reference
    }
}