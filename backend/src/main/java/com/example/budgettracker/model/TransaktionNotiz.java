package com.example.budgettracker.model;

public class TransaktionNotiz extends Transaktion {
    private String jahrTransaktion;
    private String monatTransaktion;
    private String notiz;

    public TransaktionNotiz(String jahrTransaktion, String monatTransaktion, String notiz) {
        this.jahrTransaktion = jahrTransaktion;
        this.monatTransaktion = monatTransaktion;
        this.notiz = notiz;
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
