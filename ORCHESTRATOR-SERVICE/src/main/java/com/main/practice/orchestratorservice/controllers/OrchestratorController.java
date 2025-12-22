package com.main.practice.orchestratorservice.controllers;


import com.main.practice.orchestratorservice.dto.TransferRequestDto;
import com.main.practice.orchestratorservice.service.SagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orchestrator")
public class OrchestratorController {

    @Autowired
    private SagaService sagaService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDto request) {
        // Simple Validation
        if (request.getAmount().doubleValue() <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }

        String result = sagaService.processTransaction(request);

        if (result.contains("Failed")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
}