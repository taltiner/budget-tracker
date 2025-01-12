package com.example.budgettracker.service;

import com.example.budgettracker.model.*;
import com.example.budgettracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransaktionService {

    private final TransaktionEinnahmeRepository transaktionEinnahmeRepository;
    private final TransaktionAusgabeRepository transaktionAusgabeRepository;
    private final TransaktionNotizRepository transaktionNotizRepository;

    @Autowired
    public TransaktionService(
            TransaktionEinnahmeRepository transaktionEinnahmeRepository,
            TransaktionAusgabeRepository transaktionAusgabeRepository,
            TransaktionNotizRepository transaktionNotizRepository) {
        this.transaktionEinnahmeRepository = transaktionEinnahmeRepository;
        this.transaktionAusgabeRepository = transaktionAusgabeRepository;
        this.transaktionNotizRepository = transaktionNotizRepository;
    }

    public TransaktionEinnahme createEinnahmeTransaktion(TransaktionEinnahme einnahme) {
         return transaktionEinnahmeRepository.save(einnahme);
    }

    public List<TransaktionEinnahme> getAllTransaktionEinnahmen() {
        return transaktionEinnahmeRepository.findAll();
    }

    public TransaktionAusgabe createAusgabeTransaktion(TransaktionAusgabe ausgabe) {
        return transaktionAusgabeRepository.save(ausgabe);
    }

    public List<TransaktionAusgabe> getAllTransaktionAusgaben() {
        return transaktionAusgabeRepository.findAll();
    }

    public TransaktionNotiz createNotizTransaktion(TransaktionNotiz notiz) {
        return transaktionNotizRepository.save(notiz);
    }

    public List<TransaktionNotiz> getAllTransaktionNotiz() {
        return transaktionNotizRepository.findAll();
    }


    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = getAllTransaktionEinnahmen();
        List<TransaktionAusgabe> ausgaben = getAllTransaktionAusgaben();
        List<TransaktionNotiz> notizen = getAllTransaktionNotiz();

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);
    }
}
