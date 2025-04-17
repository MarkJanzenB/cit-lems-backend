package com.capstone.LEMS.Entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "BatchResupply")
public class BatchResupplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resupply_id")
    private int resupplyId;

    @Column(name = "date_resupply")
    private LocalDate dateResupply;

    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    private UserEntity addedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "batchResupply", cascade = CascadeType.ALL)
    private List<ItemEntity> items;

    public BatchResupplyEntity() {
        super();
    }

    public BatchResupplyEntity(int resupplyId, LocalDate dateResupply, UserEntity addedBy) {
        super();
        this.resupplyId = resupplyId;
        this.dateResupply = dateResupply;
        this.addedBy = addedBy;
    }

    @JsonProperty("resupply_id")
    public int getResupplyId() {
        return resupplyId;
    }

    public void setResupplyId(int resupplyId) {
        this.resupplyId = resupplyId;
    }

    @JsonProperty("date_resupply")
    public LocalDate getDateResupply() {
        return dateResupply;
    }

    public void setDateResupply(LocalDate dateResupply) {
        this.dateResupply = dateResupply;
    }

    @JsonProperty("added_by")
    public UserEntity getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(UserEntity addedBy) {
        this.addedBy = addedBy;
    }
}