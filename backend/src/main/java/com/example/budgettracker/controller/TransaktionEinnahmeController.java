package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionEinnahme;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transaktionen/einnahmen")
public class TransaktionEinnahmeController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionEinnahmeController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<TransaktionEinnahme> createEinnahmeTransaktion(@RequestBody TransaktionEinnahme einnahme) {
        TransaktionEinnahme savedEinnahme = transaktionService.createEinnahmeTransaktion(einnahme);
        return new ResponseEntity<>(savedEinnahme, HttpStatus.CREATED);
    }
}
