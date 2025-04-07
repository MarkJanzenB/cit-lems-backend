package com.capstone.LEMS.Repository;

import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TeacherScheduleRepository extends JpaRepository<TeacherScheduleEntity, Integer> {
    // Custom finder methods that are used in your service
    // In TeacherScheduleRepository.java
    List<TeacherScheduleEntity> findByTeacher(UserEntity teacher);
    List<TeacherScheduleEntity> findByLabNum(String labNum);
    List<TeacherScheduleEntity> findByDate(Date date);
}