package com.example.budgettracker.controller;

import com.example.budgettracker.dto.TransaktionNotizDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.TransaktionNotiz;
import com.example.budgettracker.service.TransaktionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="/transaktionen/notizen")
public class TransaktionNotizController {

    @Autowired
    private final TransaktionService transaktionService;
    private final TransaktionMapper transaktionMapper;

    public TransaktionNotizController(TransaktionService transaktionService, TransaktionMapper transaktionMapper) {
        this.transaktionService = transaktionService;
        this.transaktionMapper = transaktionMapper;
    }

    @PostMapping
    public ResponseEntity<TransaktionNotizDTO> createTransaktionNotiz(@RequestBody TransaktionNotizDTO notizDTO) {
        TransaktionNotiz savedNotiz = transaktionService.createNotizTransaktion(notizDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionNotizResponseDTO(savedNotiz), HttpStatus.CREATED);
    }

    public ResponseEntity<TransaktionNotizDTO> updateTransaktionNotiz(@RequestBody TransaktionNotizDTO notizDTO) {
        TransaktionNotiz updatedNotiz = transaktionService.createNotizTransaktion(notizDTO);
        return new ResponseEntity<>(transaktionMapper.toTransaktionNotizResponseDTO(updatedNotiz), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransaktionNotizDTO>> getAllTransaktionNotizen() {
        List<TransaktionNotiz> alleNotizen = transaktionService.getAllTransaktionNotizen();
        List<TransaktionNotizDTO> notizResponseDTO = alleNotizen.stream()
                .map(notiz -> transaktionMapper.toTransaktionNotizResponseDTO(notiz))
                .collect(Collectors.toList());
        return new ResponseEntity<>(notizResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTransaktionNotizen() {
        transaktionService.deleteAllTransaktionNotizen();
        return ResponseEntity.noContent().build();
    }

}
