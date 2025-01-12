package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionUebersicht;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/transaktionen")
public class TransaktionUebersichtController {

    private TransaktionService transaktionService;

    public TransaktionUebersichtController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

/*    @PostMapping
    public ResponseEntity<TransaktionUebersicht> createTransaktion(@RequestBody TransaktionUebersicht transaktion) {
        TransaktionUebersicht savedTransaktion = transaktionService.createTransaktion(transaktion);
        return new ResponseEntity<>(savedTransaktion, HttpStatus.CREATED);
    }*/

}
