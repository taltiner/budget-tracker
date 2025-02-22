package com.example.budgettracker.controller;

import com.example.budgettracker.dto.request.TransaktionAusgabeRequestDTO;
import com.example.budgettracker.dto.response.TransaktionAusgabeResponseDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/transaktionen/ausgaben")
public class TransaktionAusgabeController {

    @Autowired
    private final TransaktionService transaktionService;
    @Autowired
    private final TransaktionMapper transaktionMapper;
    public TransaktionAusgabeController(TransaktionService transaktionService, TransaktionMapper transaktionMapper) {
        this.transaktionService = transaktionService;
        this.transaktionMapper = transaktionMapper;
    }

    @PostMapping
    public ResponseEntity<TransaktionAusgabeResponseDTO> createAusgabeTransaktion(@RequestBody TransaktionAusgabeRequestDTO ausgabeDTO) {
        TransaktionAusgabe savedAusgabe = transaktionService.createAusgabeTransaktion(ausgabeDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionAusgabeResponseDTO(savedAusgabe), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<List<TransaktionAusgabeResponseDTO>> updateAusgabeTransaktion(@RequestBody List<TransaktionAusgabeRequestDTO> ausgabenDTO) {
        List<TransaktionAusgabe> updatedAusgaben = transaktionService.updateAusgabeTransaktion(ausgabenDTO);
        List<TransaktionAusgabeResponseDTO> updatedAusgabenDTO = updatedAusgaben.stream()
                .map(updatedAusgabe -> transaktionMapper.toTransaktionAusgabeResponseDTO(updatedAusgabe))
                .collect(Collectors.toList());
        return new ResponseEntity<>(updatedAusgabenDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransaktionAusgabeResponseDTO>> getAllTransaktionAusgaben() {
        List<TransaktionAusgabe> alleAusgaben = transaktionService.getAllTransaktionAusgaben();
        List<TransaktionAusgabeResponseDTO> alleAusgabenResponseDTO = alleAusgaben.stream()
                .map(ausgabe -> transaktionMapper.toTransaktionAusgabeResponseDTO(ausgabe))
                .collect(Collectors.toList());
        return new ResponseEntity<>(alleAusgabenResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionAusgaben() {
        transaktionService.deleteAllTransaktionAusgaben();
        return ResponseEntity.noContent().build();
    }
}
