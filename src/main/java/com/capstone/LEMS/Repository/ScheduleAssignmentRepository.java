package com.capstone.LEMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capstone.LEMS.Entity.ScheduleAssignmentEntity;

@Repository
public interface ScheduleAssignmentRepository extends JpaRepository<ScheduleAssignmentEntity, Integer>{

}
