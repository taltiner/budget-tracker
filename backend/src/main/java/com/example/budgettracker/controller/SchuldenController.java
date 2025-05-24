package com.example.budgettracker.controller;

import com.example.budgettracker.dto.SchuldenDTO;
import com.example.budgettracker.dto.TransaktionAusgabeDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.Schulden;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<SchuldenDTO>> createSchuldenEintraege(@RequestBody List<SchuldenDTO> schuldenDTOS) {
        List<SchuldenDTO> savedSchuldenDTOS = this.transaktionService.createSchuldenEintraege(schuldenDTOS);

        return new ResponseEntity<>(savedSchuldenDTOS, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<List<SchuldenDTO>> updateSchuldenEintraege(@RequestBody List<SchuldenDTO> schuldenDTOS) {
        List<SchuldenDTO> updatedSchuldenDTOS = this.transaktionService.updateSchuldenEintraege(schuldenDTOS);

        return new ResponseEntity<>(updatedSchuldenDTOS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SchuldenDTO>> getSchuldenEintraege() {
        List<SchuldenDTO> schuldenDTOS = this.transaktionService.getSchuldenEintraege();

        return new ResponseEntity<>(schuldenDTOS, HttpStatus.OK);
    }
}
