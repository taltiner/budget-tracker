package com.example.budgettracker.controller;

import com.example.budgettracker.dto.TransaktionEinnahmeDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.TransaktionEinnahme;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transaktionen/einnahmen")
public class TransaktionEinnahmeController {

    @Autowired
    private final TransaktionService transaktionService;
    @Autowired
    private final TransaktionMapper transaktionMapper;

    public TransaktionEinnahmeController(
            TransaktionService transaktionService,
            TransaktionMapper transaktionMapper) {
        this.transaktionService = transaktionService;
        this.transaktionMapper = transaktionMapper;
    }

    @PostMapping
    public ResponseEntity<TransaktionEinnahmeDTO> createEinnahmeTransaktion(@RequestBody TransaktionEinnahmeDTO einnahmeDTO) {
        TransaktionEinnahme savedEinnahme = transaktionService.createEinnahmeTransaktion(einnahmeDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionEinnahmeResponseDTO(savedEinnahme), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransaktionEinnahmeDTO> updateEinnahmeTransaktion(@RequestBody TransaktionEinnahmeDTO einnahmeDTO) {
        TransaktionEinnahme updatedEinnahme = transaktionService.updateEinnahmeTransaktion(einnahmeDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionEinnahmeResponseDTO(updatedEinnahme), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List <TransaktionEinnahmeDTO>> getAllTransaktionEinnahmen() {
        List<TransaktionEinnahme> alleEinnahmen = transaktionService.getAllTransaktionEinnahmen();
        List<TransaktionEinnahmeDTO> alleEinnahmenResponseDTO =  alleEinnahmen.stream()
                .map(einnahme -> transaktionMapper.toTransaktionEinnahmeResponseDTO(einnahme))
                .collect(Collectors.toList());
        return new ResponseEntity<>(alleEinnahmenResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionEinnahmen() {
        transaktionService.deleteAllTransaktionEinnahmen();
        return ResponseEntity.noContent().build();
    }
}
