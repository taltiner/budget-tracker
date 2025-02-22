package com.example.budgettracker.controller;

import com.example.budgettracker.dto.request.TransaktionEinnahmeRequestDTO;
import com.example.budgettracker.dto.response.TransaktionEinnahmeResponseDTO;
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
    public ResponseEntity<TransaktionEinnahmeResponseDTO> createEinnahmeTransaktion(@RequestBody TransaktionEinnahmeRequestDTO einnahmeDTO) {
        TransaktionEinnahme savedEinnahme = transaktionService.createEinnahmeTransaktion(einnahmeDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionEinnahmeResponseDTO(savedEinnahme), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransaktionEinnahmeResponseDTO> updateEinnahmeTransaktion(@RequestBody TransaktionEinnahmeRequestDTO einnahmeDTO) {
        TransaktionEinnahme updatedEinnahme = transaktionService.updateEinnahmeTransaktion(einnahmeDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionEinnahmeResponseDTO(updatedEinnahme), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List <TransaktionEinnahmeResponseDTO>> getAllTransaktionEinnahmen() {
        List<TransaktionEinnahme> alleEinnahmen = transaktionService.getAllTransaktionEinnahmen();
        List<TransaktionEinnahmeResponseDTO> alleEinnahmenResponseDTO =  alleEinnahmen.stream()
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
