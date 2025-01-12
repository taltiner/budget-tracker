package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.repository.TransaktionAusgabeRepository;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transaktionen/ausgaben")
public class TransaktionAusgabeController {

    @Autowired
    private TransaktionService transaktionService;

    public TransaktionAusgabeController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<TransaktionAusgabe> createAusgabeTransaktion(@RequestBody TransaktionAusgabe ausgabe) {
        TransaktionAusgabe savedAusgabe = transaktionService.createAusgabeTransaktion(ausgabe);
        return new ResponseEntity<>(savedAusgabe, HttpStatus.CREATED);
    }
}
