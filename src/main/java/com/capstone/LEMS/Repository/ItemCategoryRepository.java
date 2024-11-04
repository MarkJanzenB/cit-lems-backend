package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.ItemCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.ItemCategoryEntity;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategoryEntity, Integer> {
}
