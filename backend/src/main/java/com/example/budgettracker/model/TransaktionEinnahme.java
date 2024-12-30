package com.example.budgettracker.model;

public class TransaktionEinnahme extends Transaktion {
    private String jahrTransaktion;
    private String monatTransaktion;
    private Geldbetrag betragEinnahme;

    public TransaktionEinnahme(String jahrTransaktion, String monatTransaktion, Geldbetrag betragEinnahme) {
        this.jahrTransaktion = jahrTransaktion;
        this.monatTransaktion = monatTransaktion;
        this.betragEinnahme = betragEinnahme;
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
