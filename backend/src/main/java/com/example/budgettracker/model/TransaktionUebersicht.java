package com.example.budgettracker.model;

import java.util.List;

public class TransaktionUebersicht {
    private List<TransaktionEinnahme> einnahmen;
    private List<TransaktionAusgabe> ausgaben;
    private List<TransaktionNotiz> notizen;

    public TransaktionUebersicht(List<TransaktionEinnahme> einnahmen,
                                 List<TransaktionAusgabe> ausgaben,
                                 List<TransaktionNotiz> notizen) {
        this.einnahmen = einnahmen;
        this.ausgaben = ausgaben;
        this.notizen = notizen;
    }

    public List<TransaktionEinnahme> getEinnahmen() {
        return einnahmen;
    }
    public List<TransaktionAusgabe> getAusgaben() {
        return ausgaben;
    }
    public List<TransaktionNotiz> getNotizen() {
        return notizen;
    }

    public void setEinnahmen(List<TransaktionEinnahme> einnahmen) {
        this.einnahmen = einnahmen;
    }
    public void setAusgaben(List<TransaktionAusgabe> ausgaben) {
        this.ausgaben = ausgaben;
    }
    public void setNotizen(List<TransaktionNotiz> notizen) {
        this.notizen = notizen;
    }

}
