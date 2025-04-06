package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.ManufacturerEntity;

@Repository
public interface ManufacturerRepository extends JpaRepository<ManufacturerEntity, Integer>{

}
