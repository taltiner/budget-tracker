package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSAKTION_AUSGABE")
public class TransaktionAusgabe extends Transaktion{
    @Id
    private Long id;
    private String kategorie;
    private String benutzerdefinierteKategorie;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragAusgabe;


    public TransaktionAusgabe(
            EingabeArt transaktionsArt,
            String kategorie,
            String benutzerdefinierteKategorie,
            Geldbetrag betragAusgabe,
            String jahrTransaktion,
            String monatTransaktion) {
        super(transaktionsArt, jahrTransaktion, monatTransaktion);
        this.kategorie = kategorie;
        this.benutzerdefinierteKategorie = benutzerdefinierteKategorie;
        this.betragAusgabe = betragAusgabe;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
 }
