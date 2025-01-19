package com.example.budgettracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transaktionen/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain")
                .body("Server is running");
    }

}
