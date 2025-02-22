package com.example.budgettracker.dto.response;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import org.springframework.data.relational.core.mapping.Embedded;

public class TransaktionAusgabeResponseDTO {
    private EingabeArt transaktionsArt;
    private String jahrTransaktion;
    private String monatTransaktion;
    private String kategorie;
    private String benutzerdefinierteKategorie;
    private Geldbetrag betragAusgabe;

    public TransaktionAusgabeResponseDTO() {}


    public EingabeArt getTransaktionsArt() {
        return transaktionsArt;
    }
    public String getJahrTransaktion() {
        return jahrTransaktion;
    }
    public String getMonatTransaktion() {
        return monatTransaktion;
    }
    public String getKategorie() {
        return kategorie;
    }
    public String getBenutzerdefinierteKategorie() {
        return benutzerdefinierteKategorie;
    }
    public Geldbetrag getBetragAusgabe() {
        return betragAusgabe;
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
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }
    public void setBenutzerdefinierteKategorie(String benutzerdefinierteKategorie) {
        this.benutzerdefinierteKategorie = kategorie;
    }
    public void setBetragAusgabe(Geldbetrag betragAusgabe) {
        this.betragAusgabe = betragAusgabe;
    }

}
