package com.capstone.LEMS.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import javax.swing.*;

@Entity
public class GroupEntity {

    @Id
    @GeneratedValue
    private int group_id;
    @Column(name = "group_name")
    private String group_name;

    public GroupEntity(){}

    public GroupEntity(int group_id, String group_name){
        this.group_id = group_id;
        this.group_name = group_name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
