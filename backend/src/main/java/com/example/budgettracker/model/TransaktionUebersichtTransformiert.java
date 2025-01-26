package com.example.budgettracker.model;

import java.util.Map;

public class TransaktionUebersichtTransformiert {
    private GeldbetragNumerisch einnahmen;
    private Map<String, GeldbetragNumerisch> ausgaben;
    private String monatTransaktion;
    private GeldbetragNumerisch gesamtausgaben;
    private GeldbetragNumerisch saldo;
    private String notiz;

    public TransaktionUebersichtTransformiert(Builder builder) {
        this.einnahmen = builder.einnahmen;
        this.ausgaben = builder.ausgaben;
        this.monatTransaktion = builder.monatTransaktion;
        this.gesamtausgaben = builder.gesamtausgaben;
        this.saldo = builder.saldo;
        this.notiz = builder.notiz;
    }

    public GeldbetragNumerisch getEinnahmen() {
        return einnahmen;
    }
    public Map<String, GeldbetragNumerisch> getAusgaben() {
        return ausgaben;
    }
    public String getMonatTransaktion() {
        return monatTransaktion;
    }
    public GeldbetragNumerisch getGesamtausgaben() {
        return gesamtausgaben;
    }
    public GeldbetragNumerisch getSaldo() {
        return saldo;
    }
    public String getNotiz() {
        return notiz;
    }

    public void setEinnahmen(GeldbetragNumerisch einnahmen) {
        this.einnahmen = einnahmen;
    }
    public void setAusgaben(Map<String, GeldbetragNumerisch> ausgaben) {
        this.ausgaben = ausgaben;
    }
    public void setMonatTransaktion(String monatTransaktion) {
        this.monatTransaktion = monatTransaktion;
    }
    public void setGesamtausgaben(GeldbetragNumerisch ausgaben) {
        this.gesamtausgaben = gesamtausgaben;
    }
    public void setSaldo(GeldbetragNumerisch saldo) {
        this.saldo = saldo;
    }
    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public static class Builder {
        private GeldbetragNumerisch einnahmen;
        private Map<String, GeldbetragNumerisch> ausgaben;
        private String monatTransaktion;
        private GeldbetragNumerisch gesamtausgaben;
        private GeldbetragNumerisch saldo;
        private String notiz;

        public Builder setEinnahmen(GeldbetragNumerisch einnahmen) {
            this.einnahmen = einnahmen;
            return this;
        }
        public Builder setAusgaben(Map<String, GeldbetragNumerisch> ausgaben) {
            this.ausgaben = ausgaben;
            return this;
        }
        public Builder setMonatTransaktion(String monatTransaktion) {
            this.monatTransaktion = monatTransaktion;
            return this;
        }
        public Builder setGesamtausgaben(GeldbetragNumerisch ausgaben) {
            this.gesamtausgaben = gesamtausgaben;
            return this;
        }
        public Builder setSaldo(GeldbetragNumerisch saldo) {
            this.saldo = saldo;
            return this;
        }
        public Builder setNotiz(String notiz) {
            this.notiz = notiz;
            return this;
        }

        public TransaktionUebersichtTransformiert build() {
            return new TransaktionUebersichtTransformiert(this);
        }
    }

}
