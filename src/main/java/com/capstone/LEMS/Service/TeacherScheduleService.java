package com.capstone.LEMS.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Repository.TeacherScheduleRepository;
@Service
public class TeacherScheduleService {
    @Autowired
    TeacherScheduleRepository teacherScheduleRepository;
    public List<TeacherScheduleEntity> getAllTeacherSchedules() {
        return teacherScheduleRepository.findAll();
    }
    
    public TeacherScheduleEntity AddTeacherSchedule(TeacherScheduleEntity teachsched) {
    	return teacherScheduleRepository.save(teachsched);
    }
}
