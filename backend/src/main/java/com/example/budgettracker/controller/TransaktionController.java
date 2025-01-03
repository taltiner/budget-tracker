package com.example.budgettracker.controller;

import com.example.budgettracker.model.*;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transaktionen")
public class TransaktionController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }
    @GetMapping
    public TransaktionUebersicht getAllTransaktionen() {
        return this.transaktionService.getAllTransaktionen();
    }

}
