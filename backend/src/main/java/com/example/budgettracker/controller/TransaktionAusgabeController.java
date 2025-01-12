package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/transaktionen/ausgaben")
public class TransaktionAusgabeController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionAusgabeController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<TransaktionAusgabe> createAusgabeTransaktion(@RequestBody TransaktionAusgabe ausgabe) {
        TransaktionAusgabe savedAusgabe = transaktionService.createAusgabeTransaktion(ausgabe);
        return new ResponseEntity<>(savedAusgabe, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransaktionAusgabe>> getAllTransaktionAusgaben() {
        List<TransaktionAusgabe> alleAusgaben = transaktionService.getAllTransaktionAusgaben();
        return new ResponseEntity<>(alleAusgaben, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionAusgaben() {
        transaktionService.deleteAllTransaktionAusgaben();
        return ResponseEntity.noContent().build();
    }
}
