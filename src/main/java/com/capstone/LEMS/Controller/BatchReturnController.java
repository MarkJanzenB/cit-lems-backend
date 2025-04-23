package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Entity.BatchReturnEntity;
import com.capstone.LEMS.Service.BatchReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batchreturn")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class BatchReturnController {

    @Autowired
    private BatchReturnService batchReturnService;

    @PostMapping("/add")
    public BatchReturnEntity addBatchReturn(@RequestBody BatchReturnEntity batchReturn) {
        return batchReturnService.addBatchReturn(batchReturn);
    }

    @GetMapping("/all")
    public List<BatchReturnEntity> getAllBatchReturns() {
        return batchReturnService.getAllBatchReturns();
    }
}