package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.RequestEntity;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Integer>{
List<RequestEntity> findByStatus(String status);
}
