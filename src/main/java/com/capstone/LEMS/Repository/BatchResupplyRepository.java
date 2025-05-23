package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Entity.UserEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchResupplyRepository extends JpaRepository<BatchResupplyEntity, Integer> {
	
	List<BatchResupplyEntity> findByDateResupplyAndAddedBy(LocalDate dateResupply, UserEntity addedBy);
	@Query("SELECT DISTINCT br.dateResupply, br.addedBy FROM BatchResupplyEntity br")
	List<Object[]> findDistinctByDateResupplyAndAddedBy();
}