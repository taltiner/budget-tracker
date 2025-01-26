package com.example.budgettracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSAKTION_NOTIZ")
public class TransaktionNotiz extends Transaktion {
    @Id
    private Long id;
    private String notiz;

    public TransaktionNotiz(
            EingabeArt transaktionsArt,
            String jahrTransaktion,
            String monatTransaktion,
            String notiz) {
        super(transaktionsArt, jahrTransaktion, monatTransaktion);
        super.setTransaktionsArt(transaktionsArt);
        super.setJahrTransaktion(jahrTransaktion);
        super.setMonatTransaktion(monatTransaktion);
        this.notiz = notiz;
    }

    public Long getId() {
        return id;
    }
    public String getNotiz() {
        return notiz;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}
