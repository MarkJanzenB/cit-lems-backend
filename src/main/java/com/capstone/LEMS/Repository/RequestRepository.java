package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.RequestEntity;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Integer>{

}
