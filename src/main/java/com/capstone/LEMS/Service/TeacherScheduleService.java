package com.capstone.LEMS.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.capstone.LEMS.Entity.TeacherScheduleEntity;
import com.capstone.LEMS.Entity.YearSectionEntity;
import com.capstone.LEMS.Repository.TeacherScheduleRepository;
import com.capstone.LEMS.Repository.YearSectionRepository;

@Service
public class TeacherScheduleService {
    @Autowired
    TeacherScheduleRepository teacherScheduleRepository;

    @Autowired
    YearSectionRepository yearSectionRepository;

    private static final Logger log = LoggerFactory.getLogger(TeacherScheduleService.class);

    public List<TeacherScheduleEntity> getAllTeacherSchedules() {
        return teacherScheduleRepository.findAll();
    }

    public TeacherScheduleEntity AddTeacherSchedule(TeacherScheduleEntity teachsched) {
        log.info("Adding new teacher schedule");
        teachsched.setDateCreated(LocalDateTime.now());
        // You would typically get the current user from the security context
        // teachsched.setCreatedBy(currentUser);
        return teacherScheduleRepository.save(teachsched);
    }

    public ResponseEntity<?> getScheduleById(int scheduleId) {
        log.info("Fetching schedule with ID: {}", scheduleId);
        Optional<TeacherScheduleEntity> schedule = teacherScheduleRepository.findById(scheduleId);

        if(schedule.isPresent()) {
            return ResponseEntity.ok(schedule.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Schedule with ID " + scheduleId + " not found");
        }
    }

    public ResponseEntity<?> getSchedulesByTeacherId(int teacherId) {
        log.info("Fetching schedules for teacher ID: {}", teacherId);
        List<TeacherScheduleEntity> schedules = teacherScheduleRepository.findByTeacherId(teacherId);

        if(schedules.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No schedules found for teacher ID " + teacherId);
        }

        return ResponseEntity.ok(schedules);
    }

    public ResponseEntity<?> getSchedulesByLabNum(int labNum) {
        log.info("Fetching schedules for lab number: {}", labNum);
        List<TeacherScheduleEntity> schedules = teacherScheduleRepository.findByLabNum(labNum);

        if(schedules.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No schedules found for lab number " + labNum);
        }

        return ResponseEntity.ok(schedules);
    }

    public ResponseEntity<?> getSchedulesByDate(Date date) {
        log.info("Fetching schedules for date: {}", date);
        List<TeacherScheduleEntity> schedules = teacherScheduleRepository.findByDate(date);

        if(schedules.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No schedules found for the given date");
        }

        return ResponseEntity.ok(schedules);
    }

    public ResponseEntity<?> updateSchedule(int scheduleId, TeacherScheduleEntity newSchedule) {
        log.info("Updating schedule with ID: {}", scheduleId);

        Optional<TeacherScheduleEntity> scheduleOptional = teacherScheduleRepository.findById(scheduleId);

        if(scheduleOptional.isPresent()) {
            TeacherScheduleEntity schedule = scheduleOptional.get();

            if(newSchedule.getStartTime() != null) {
                schedule.setStartTime(newSchedule.getStartTime());
            }

            if(newSchedule.getEndTime() != null) {
                schedule.setEndTime(newSchedule.getEndTime());
            }

            if(newSchedule.getLabNum() != 0) {
                schedule.setLabNum(newSchedule.getLabNum());
            }

            if(newSchedule.getDate() != null) {
                schedule.setDate(newSchedule.getDate());
            }

            if(newSchedule.getTeacherId() != 0) {
                schedule.setTeacherId(newSchedule.getTeacherId());
            }

            if(newSchedule.getYearSection() != null && newSchedule.getYearSection().getYearId() != 0) {
                log.info("Updating year section with ID: {}", newSchedule.getYearSection().getYearId());
                Optional<YearSectionEntity> yearSectionOptional = yearSectionRepository.findById(newSchedule.getYearSection().getYearId());

                if(yearSectionOptional.isPresent()) {
                    schedule.setYearSection(yearSectionOptional.get());
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("Year Section with ID " + newSchedule.getYearSection().getYearId() + " not found");
                }
            }

            return ResponseEntity.ok(teacherScheduleRepository.save(schedule));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Schedule with ID " + scheduleId + " not found");
        }
    }

    public ResponseEntity<?> deleteSchedule(int scheduleId) {
        log.info("Deleting schedule with ID: {}", scheduleId);

        if(teacherScheduleRepository.existsById(scheduleId)) {
            teacherScheduleRepository.deleteById(scheduleId);
            return ResponseEntity.ok("Schedule successfully deleted");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Schedule with ID " + scheduleId + " not found");
        }
    }
}