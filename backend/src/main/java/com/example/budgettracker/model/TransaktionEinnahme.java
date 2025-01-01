package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "TRANSAKTION_EINNAHME")
public class TransaktionEinnahme extends Transaktion {
    @Id
    private Long id;
    private String jahrTransaktion;
    private String monatTransaktion;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragEinnahme;

    public TransaktionEinnahme(
            EingabeArt transaktionsArt,
            String jahrTransaktion,
            String monatTransaktion,
            Geldbetrag betragEinnahme) {
        super(transaktionsArt);
        this.jahrTransaktion = jahrTransaktion;
        this.monatTransaktion = monatTransaktion;
        this.betragEinnahme = betragEinnahme;
    }


    public Long getId() {
        return id;
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
    public void setId(Long id) {
        this.id = id;
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
