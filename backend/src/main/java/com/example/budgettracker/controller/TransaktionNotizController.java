package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionNotiz;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/transaktionen/notizen")
public class TransaktionNotizController {

    private TransaktionService transaktionService;

    public TransaktionNotizController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<TransaktionNotiz> createTransaktionNotiz(@RequestBody TransaktionNotiz notiz) {
        TransaktionNotiz savedNotiz = transaktionService.createNotizTransaktion(notiz);
        return new ResponseEntity<>(savedNotiz, HttpStatus.CREATED);
    }

}
