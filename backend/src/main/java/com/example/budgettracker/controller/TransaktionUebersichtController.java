package com.example.budgettracker.controller;

import com.example.budgettracker.dto.response.TransaktionAusgabeResponseDTO;
import com.example.budgettracker.dto.response.TransaktionUebersichtResponseDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.TransaktionUebersicht;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path="/transaktionen")
public class TransaktionUebersichtController {

    @Autowired
    private final TransaktionService transaktionService;
    private final TransaktionMapper transaktionMapper;

    public TransaktionUebersichtController(TransaktionService transaktionService, TransaktionMapper transaktionMapper) {
        this.transaktionService = transaktionService;
        this.transaktionMapper = transaktionMapper;
    }

    @GetMapping
    public ResponseEntity<TransaktionUebersichtResponseDTO> getTransaktion(@RequestParam String monat, @RequestParam String jahr) {
        TransaktionUebersicht transaktion = transaktionService.getTransaktion(monat, jahr);
        return new ResponseEntity<>(transaktionMapper.toTransaktionUebersichtResponseDTO(transaktion), HttpStatus.OK);
    }

    @GetMapping("/alle")
    public ResponseEntity<TransaktionUebersichtResponseDTO> getAllTransaktionen() {
        TransaktionUebersicht uebersicht = transaktionService.getAllTransaktionen();
        return new ResponseEntity<>(transaktionMapper.toTransaktionUebersichtResponseDTO(uebersicht), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteTransaktion(@RequestParam String monat, @RequestParam String jahr) {
        Map<String, String> response = new HashMap<>();
            transaktionService.deleteTransaktion(monat, jahr);
            response.put("message", "Transaktion erfolgreich gelöscht");
            return ResponseEntity.ok(response);
    }

}
