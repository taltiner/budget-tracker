package com.example.budgettracker.dto.request;

import com.example.budgettracker.model.EingabeArt;

public class TransaktionNotizRequestDTO {
    private EingabeArt transaktionsArt;
    private String jahrTransaktion;
    private String monatTransaktion;
    private String notiz;


    public TransaktionNotizRequestDTO() {}


    public EingabeArt getTransaktionsArt() {
        return transaktionsArt;
    }
    public String getJahrTransaktion() {
        return jahrTransaktion;
    }
    public String getMonatTransaktion() {
        return monatTransaktion;
    }
    public String getNotiz() {
        return notiz;
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
    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}
