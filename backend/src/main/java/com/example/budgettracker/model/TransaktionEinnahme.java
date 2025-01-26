package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "TRANSAKTION_EINNAHME")
public class TransaktionEinnahme extends Transaktion {
    @Id
    private Long id;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragEinnahme;

    public TransaktionEinnahme(
            EingabeArt transaktionsArt,
            String jahrTransaktion,
            String monatTransaktion,
            Geldbetrag betragEinnahme) {
        super(transaktionsArt, jahrTransaktion, monatTransaktion);
        this.betragEinnahme = betragEinnahme;
    }


    public Long getId() {
        return id;
    }

    public Geldbetrag getBetragEinnahme() {
        return betragEinnahme;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setBetragEinnahme(Geldbetrag betragEinnahme) {
        this.betragEinnahme = betragEinnahme;
    }
}
