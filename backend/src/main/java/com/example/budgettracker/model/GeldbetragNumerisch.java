package com.example.budgettracker.model;

public class GeldbetragNumerisch {
    private double hoehe;
    private String waehrung;

    public GeldbetragNumerisch(double hoehe, String waehrung) {
        this.hoehe = hoehe;
        this.waehrung = waehrung;
    }

    public double getHoehe() {
        return hoehe;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public void setHoehe(double hoehe) {
        this.hoehe = hoehe;
    }


    public void setWaehrung(String waehrung) {
        this.waehrung = waehrung;
    }

    public void plus(double betrag) {
        this.hoehe += betrag;
    }

    public void minus(double betrag) {
        this.hoehe -= betrag;
    }

}
