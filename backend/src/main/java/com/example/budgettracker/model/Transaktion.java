package com.example.budgettracker.model;

public class Transaktion {

    private EingabeArt transaktionsArt;
    private String jahrTransaktion;
    private String monatTransaktion;


    public Transaktion(EingabeArt transaktionsArt, String jahrTransaktion, String monatTransaktion) {
        this.transaktionsArt = transaktionsArt;
        this.monatTransaktion = monatTransaktion;
        this.jahrTransaktion = jahrTransaktion;
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

    public void setTransaktionsArt(EingabeArt transaktionsArt) {
        this.transaktionsArt = transaktionsArt;
    }
    public void setJahrTransaktion(String jahrTransaktion) {
        this.jahrTransaktion = jahrTransaktion;
    }
    public void setMonatTransaktion(String monatTransaktion) {
        this.monatTransaktion = monatTransaktion;
    }
}
