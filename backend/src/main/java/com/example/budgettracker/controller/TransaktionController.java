package com.example.budgettracker.controller;

import com.example.budgettracker.model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/transaktion")
public class TransaktionController {

    @GetMapping
    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = new ArrayList<>();
        List<TransaktionAusgabe> ausgaben = new ArrayList<>();
        List<TransaktionNotiz> notizen = new ArrayList<>();
        einnahmen.add(new TransaktionEinnahme("2025", "Januar", new Geldbetrag("3000", "€")));
        ausgaben.add(new TransaktionAusgabe("2025-01-01", "Test","Test",  new Geldbetrag("50", "€")));
        notizen.add(new TransaktionNotiz("2025", "Januar", "Das ist ein Test"));

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);
    }
}
