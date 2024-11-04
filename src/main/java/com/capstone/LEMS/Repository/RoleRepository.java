package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{

}
