package com.capstone.LEMS.Service;

import com.capstone.LEMS.Entity.BatchResupplyEntity;
import com.capstone.LEMS.Repository.BatchResupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchResupplyService {

    @Autowired
    private BatchResupplyRepository batchResupplyRepository;

    public BatchResupplyEntity addBatchResupply(BatchResupplyEntity batchResupply) {
        return batchResupplyRepository.save(batchResupply);
    }

    public List<BatchResupplyEntity> getAllBatchResupplies() {
        return batchResupplyRepository.findAll();
    }
}