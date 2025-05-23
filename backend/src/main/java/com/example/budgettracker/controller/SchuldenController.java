package com.example.budgettracker.controller;

import com.example.budgettracker.dto.SchuldenDTO;
import com.example.budgettracker.dto.TransaktionAusgabeDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.Schulden;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "transaktionen/schulden")
public class SchuldenController {

    private final TransaktionService transaktionService;
    private final TransaktionMapper transaktionMapper;

    public SchuldenController(TransaktionService transaktionService,
                              TransaktionMapper transaktionMapper) {
        this.transaktionService = transaktionService;
        this.transaktionMapper = transaktionMapper;
    }

    @PostMapping
    public ResponseEntity<List<SchuldenDTO>> createSchuldenEintrag(@RequestBody List<SchuldenDTO> schuldenDTO) {
        List<Schulden> savedSchulden = this.transaktionService.createSchuldenEintrag(schuldenDTO);

        List<SchuldenDTO> savedSchuldenDTO = savedSchulden.stream()
                .map(savedSchuld -> transaktionMapper.toSchuldenDTO(savedSchuld))
                .collect(Collectors.toList());
        return new ResponseEntity<>(savedSchuldenDTO, HttpStatus.OK);
    }
}
