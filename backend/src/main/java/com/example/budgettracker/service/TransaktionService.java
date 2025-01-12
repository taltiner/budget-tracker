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

    public void deleteAllTransaktionEinnahmen() {
        transaktionEinnahmeRepository.deleteAll();
    }

    public TransaktionAusgabe createAusgabeTransaktion(TransaktionAusgabe ausgabe) {
        return transaktionAusgabeRepository.save(ausgabe);
    }

    public List<TransaktionAusgabe> getAllTransaktionAusgaben() {
        return transaktionAusgabeRepository.findAll();
    }

    public void deleteAllTransaktionAusgaben() {
        transaktionAusgabeRepository.deleteAll();
    }

    public TransaktionNotiz createNotizTransaktion(TransaktionNotiz notiz) {
        return transaktionNotizRepository.save(notiz);
    }

    public List<TransaktionNotiz> getAllTransaktionNotizen() {
        return transaktionNotizRepository.findAll();
    }

    public void deleteAllTransaktionNotizen() {
        transaktionNotizRepository.deleteAll();
    }

    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = getAllTransaktionEinnahmen();
        List<TransaktionAusgabe> ausgaben = getAllTransaktionAusgaben();
        List<TransaktionNotiz> notizen = getAllTransaktionNotizen();

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);
    }
}
