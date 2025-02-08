package com.example.budgettracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "/transaktionen/toggle")
public class ToggleController {

    @GetMapping
    public ResponseEntity<List<String>> getFeatureToggles() {
        List<String> toggles = List.of("CRUD_TRANSAKTION");
        return new ResponseEntity<>(toggles, HttpStatus.OK);
    }
}
