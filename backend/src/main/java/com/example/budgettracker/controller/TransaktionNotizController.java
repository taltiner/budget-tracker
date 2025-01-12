package com.example.budgettracker.controller;

import com.example.budgettracker.model.TransaktionNotiz;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/transaktionen/notizen")
public class TransaktionNotizController {

    @Autowired
    private final TransaktionService transaktionService;

    public TransaktionNotizController(TransaktionService transaktionService) {
        this.transaktionService = transaktionService;
    }

    @PostMapping
    public ResponseEntity<TransaktionNotiz> createTransaktionNotiz(@RequestBody TransaktionNotiz notiz) {
        TransaktionNotiz savedNotiz = transaktionService.createNotizTransaktion(notiz);
        return new ResponseEntity<>(savedNotiz, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransaktionNotiz>> getAllTransaktionNotizen() {
        List<TransaktionNotiz> alleNotizen = transaktionService.getAllTransaktionNotizen();
        return new ResponseEntity<>(alleNotizen, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionNotizen() {
        transaktionService.deleteAllTransaktionNotizen();
        return ResponseEntity.noContent().build();
    }

}
