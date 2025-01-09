package com.example.budgettracker.service;

import com.example.budgettracker.model.*;
import com.example.budgettracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransaktionService {

    private final TransaktionRepository transaktionRepository;
    private final TransaktionEinnahmeRepository transaktionEinnahmeRepository;
    private final TransaktionAusgabeRepository transaktionAusgabeRepository;
    private final TransaktionNotizRepository transaktionNotizRepository;

    private final TransaktionUebersichtRepository transaktionUebersichtRepository;

    @Autowired
    public TransaktionService(
            TransaktionRepository transaktionRepository,
            TransaktionEinnahmeRepository transaktionEinnahmeRepository,
            TransaktionAusgabeRepository transaktionAusgabeRepository,
            TransaktionNotizRepository transaktionNotizRepository,
            TransaktionUebersichtRepository transaktionUebersichtRepository) {
        this.transaktionRepository = transaktionRepository;
        this.transaktionEinnahmeRepository = transaktionEinnahmeRepository;
        this.transaktionAusgabeRepository = transaktionAusgabeRepository;
        this.transaktionNotizRepository = transaktionNotizRepository;
        this.transaktionUebersichtRepository = transaktionUebersichtRepository;
    }

    public TransaktionEinnahme createEinnahmeTransaktion(TransaktionEinnahme einnahme) {
        return transaktionEinnahmeRepository.save(einnahme);
    }
    public TransaktionAusgabe createAusgabeTransaktion(TransaktionAusgabe ausgabe) {
        return transaktionAusgabeRepository.save(ausgabe);
    }
    public TransaktionNotiz createNotizTransaktion(TransaktionNotiz notiz) {
        return transaktionNotizRepository.save(notiz);
    }

    public TransaktionUebersicht createTransaktion(TransaktionUebersicht transaktion) {
        return transaktionUebersichtRepository.save(transaktion);
    }

    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = new ArrayList<>();
        List<TransaktionAusgabe> ausgaben = new ArrayList<>();
        List<TransaktionNotiz> notizen = new ArrayList<>();
        einnahmen.add(new TransaktionEinnahme(EingabeArt.EINNAHME,"2025", "Januar", new Geldbetrag("3000", "€")));
        ausgaben.add(new TransaktionAusgabe( EingabeArt.AUSGABE, "2025-01-01", "Test","Test",  new Geldbetrag("50", "€")));
        notizen.add(new TransaktionNotiz( EingabeArt.NOTIZ,"2025", "Januar", "Das ist ein Test"));

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);

        //return transaktionRepository.findAll();
    }
}
