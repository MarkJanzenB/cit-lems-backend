package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchIncidentsEntity;
import com.capstone.LEMS.Repository.BatchIncidentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchIncidentsService {

    @Autowired
    private BatchIncidentsRepository batchIncidentsRepository;

    public List<BatchIncidentsEntity> getAllBatchIncidents() {
        return batchIncidentsRepository.findAll();
    }

    public Optional<BatchIncidentsEntity> getBatchIncidentById(int id) {
        return batchIncidentsRepository.findById(id);
    }

    public BatchIncidentsEntity saveBatchIncident(BatchIncidentsEntity incident) {
        return batchIncidentsRepository.save(incident);
    }

    public void deleteBatchIncident(int id) {
        batchIncidentsRepository.deleteById(id);
    }

    public boolean existsById(int id) {
        return batchIncidentsRepository.existsById(id);
    }

    public List<BatchIncidentsEntity> findByBatchId(String batchId) {
        return batchIncidentsRepository.findByBatchId(batchId);
    }
}
