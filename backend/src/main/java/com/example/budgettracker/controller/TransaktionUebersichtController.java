package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionUebersicht;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/transaktionen")
public class TransaktionUebersichtController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionUebersichtController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @GetMapping
    public ResponseEntity<TransaktionUebersicht> getTransaktion(@RequestParam String monat, @RequestParam String jahr) {
        TransaktionUebersicht transaktion = transaktionService.getTransaktion(monat, jahr);
        return new ResponseEntity<>(transaktion, HttpStatus.OK);
    }

    @GetMapping("/alle")
    public ResponseEntity<TransaktionUebersicht> getAllTransaktionen() {
        TransaktionUebersicht uebersicht = transaktionService.getAllTransaktionen();
        return new ResponseEntity<>(uebersicht, HttpStatus.OK);
    }

}
