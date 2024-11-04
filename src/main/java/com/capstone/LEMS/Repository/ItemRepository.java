package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.ItemEntity;

@Repository
public interface ItemRepository  extends JpaRepository<ItemEntity, Integer>{
}
