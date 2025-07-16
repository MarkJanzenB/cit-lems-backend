// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Controller/BatchPulloutController.java
package com.capstone.LEMS.Controller;

import com.capstone.LEMS.Service.BatchPulloutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/batchpullout")
@CrossOrigin(origins = "https://cit-lems.vercel.app")
public class BatchPulloutController {

    @Autowired
    private BatchPulloutService batchPulloutService;

    @GetMapping("/combinedpullouthistory")
    public ResponseEntity<?> getCombinedPulloutHistory() {
        return batchPulloutService.getCombinedPulloutHistory();
    }

    // Optional: If you need to get items for a specific pullout date and user in the future
    @GetMapping("/getbydateandpulledby")
    public ResponseEntity<?> getByLocalDateAndPulledBy(@RequestParam LocalDate datePullout, @RequestParam int userID){
        // This method would need to be implemented in BatchPulloutService if needed
        // For now, the main combined history should suffice for the frontend tab
        return ResponseEntity.notFound().build(); // Placeholder
    }
}