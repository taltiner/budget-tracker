package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSAKTION_AUSGABE")
public class TransaktionAusgabe extends Transaktion{
    @Id
    private Long id;
    private String datumTransaktion;
    private String kategorie;
    private String benutzerdefinierteKategorie;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragAusgabe;

    private String tagTransaktion;
    private String monatTransaktion;
    private String jahrTransaktion;


    public TransaktionAusgabe(
            EingabeArt transaktionsArt,
            String datumTransaktion,
            String kategorie,
            String benutzerdefinierteKategorie,
            Geldbetrag betragAusgabe) {
        super(transaktionsArt);
        this.datumTransaktion = datumTransaktion;
        this.kategorie = kategorie;
        this.benutzerdefinierteKategorie = benutzerdefinierteKategorie;
        this.betragAusgabe = betragAusgabe;
    }

    public Long getId() {
        return id;
    }
    public String getDatumTransaktion() {
        return datumTransaktion;
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
    public String getTagTransaktion() {
        return tagTransaktion;
    }
    public String getMonatTransaktion() {
        return monatTransaktion;
    }
    public String getJahrTransaktion() {
        return jahrTransaktion;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setDatumTransaktion(String monatTransaktion) {
        this.monatTransaktion = monatTransaktion;
    }
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }
    public void setBenutzerdefinierteKategorie(String benutzerdefinierteKategorie) {
        this.benutzerdefinierteKategorie = benutzerdefinierteKategorie;
    }
    public void setBetragAusgabe(Geldbetrag betragAusgabe) {
        this.betragAusgabe = betragAusgabe;
    }
    public void setTagTransaktion(String tagTransaktion) {
        this.tagTransaktion = tagTransaktion;
    }
    public void setMonatTransaktion(String monatTransaktion) {
        this.monatTransaktion = monatTransaktion;
    }
    public void setJahrTransaktion(String jahrTransaktion) {
        this.jahrTransaktion = jahrTransaktion;
    }
 }
