package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSAKTION_NOTIZ")
public class TransaktionNotiz extends Transaktion {
    @Id
    private Long id;
    private String jahrTransaktion;
    private String monatTransaktion;
    private String notiz;

    public TransaktionNotiz(
            EingabeArt transaktionsArt,
            String jahrTransaktion,
            String monatTransaktion,
            String notiz) {
        super(transaktionsArt);
        this.jahrTransaktion = jahrTransaktion;
        this.monatTransaktion = monatTransaktion;
        this.notiz = notiz;
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
    public String getNotiz() {
        return notiz;
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
    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}
