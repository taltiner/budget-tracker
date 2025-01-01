package com.example.budgettracker.model;

public class Transaktion {

    private EingabeArt transaktionsArt;

    public Transaktion(EingabeArt transaktionsArt) {
        this.transaktionsArt = transaktionsArt;
    }

    public EingabeArt getTransaktionsArt() {
        return transaktionsArt;
    }

    public void setTransaktionsArt(EingabeArt transaktionsArt) {
        this.transaktionsArt = transaktionsArt;
    }

}
