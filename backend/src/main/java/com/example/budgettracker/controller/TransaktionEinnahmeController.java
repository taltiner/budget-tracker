package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionEinnahme;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;

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

    @PutMapping
    public ResponseEntity<TransaktionEinnahme> updateEinnahmeTransaktion(@RequestBody TransaktionEinnahme einnahme) {
        TransaktionEinnahme updatedEinnahme = transaktionService.updateEinnahmeTransaktion(einnahme);
        return new ResponseEntity<>(updatedEinnahme, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List <TransaktionEinnahme>> getAllTransaktionEinnahmen() {
        List<TransaktionEinnahme> alleEinnahmen = transaktionService.getAllTransaktionEinnahmen();
        return new ResponseEntity<>(alleEinnahmen, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionEinnahmen() {
        transaktionService.deleteAllTransaktionEinnahmen();
        return ResponseEntity.noContent().build();
    }
}
