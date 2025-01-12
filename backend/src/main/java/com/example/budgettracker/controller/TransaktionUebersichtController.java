package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionUebersicht;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/transaktionen")
public class TransaktionUebersichtController {

    private final TransaktionService transaktionService;

    public TransaktionUebersichtController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @GetMapping
    public ResponseEntity<TransaktionUebersicht> getAllTransaktionen() {
        TransaktionUebersicht uebersicht = transaktionService.getAllTransaktionen();
        return new ResponseEntity<>(uebersicht, HttpStatus.OK);
    }

}
