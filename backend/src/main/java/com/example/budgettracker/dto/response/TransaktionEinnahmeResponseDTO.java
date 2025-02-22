package com.example.budgettracker.dto.response;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;

public class TransaktionEinnahmeResponseDTO {
    private EingabeArt transaktionsArt;
    private String jahrTransaktion;
    private String monatTransaktion;
    private Geldbetrag betragEinnahme;


    public TransaktionEinnahmeResponseDTO() {

    }

    public EingabeArt getTransaktionsArt() {
        return transaktionsArt;
    }
    public String getJahrTransaktion() {
        return jahrTransaktion;
    }
    public String getMonatTransaktion() {
        return monatTransaktion;
    }
    public Geldbetrag getBetragEinnahme() {
        return betragEinnahme;
    }

    public void setTransaktionsArt(EingabeArt transaktionsArt) {
        this.transaktionsArt = transaktionsArt;
    }
    public void setJahrTransaktion(String jahrTransaktion) {
        this.jahrTransaktion = jahrTransaktion;
    }
    public void setMonatTransaktion(String monatTransaktion) {
        this.monatTransaktion = monatTransaktion;
    }
    public void setBetragEinnahme(Geldbetrag betragEinnahme) {
        this.betragEinnahme = betragEinnahme;
    }
}
