package com.example.budgettracker.controller;

import com.example.budgettracker.model.Transaktion;
import com.example.budgettracker.model.TransaktionUebersicht;
import com.example.budgettracker.model.TransaktionUebersichtTransformiert;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/transaktionen/filtered")
public class TransaktionFilteredUebersichtController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionFilteredUebersichtController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<List<TransaktionUebersichtTransformiert>> getFilteredTransaktionen(@RequestBody String selectedJahr) {
        List<TransaktionUebersichtTransformiert> filteredTransaktionen = transaktionService.getFilteredTransaktionen(selectedJahr);
        return new ResponseEntity<>(filteredTransaktionen, HttpStatus.OK);
    }
}
