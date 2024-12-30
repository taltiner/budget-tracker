package com.example.budgettracker.model;

import java.math.BigDecimal;

public class Geldbetrag {
    private String hoehe;
    private String waehrung;

    public Geldbetrag(String hoehe, String waehrung) {
        this.hoehe = hoehe;
        this.waehrung = waehrung;
    }

    public String getHoehe() {
        return hoehe;
    }

    public String getWaehrung() {
        return waehrung;
    }

    public void setHoehe(String hoehe) {
        this.hoehe = hoehe;
    }

    public void setWaehrung(String waehrung) {
        this.waehrung = waehrung;
    }
}