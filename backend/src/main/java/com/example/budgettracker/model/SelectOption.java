package com.example.budgettracker.model;

import java.util.ArrayList;
import java.util.List;

public class SelectOption {
    private String value;
    private String label;

    public SelectOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public class KategorieAuswahl {
        private static final List<SelectOption> KATEGORIE_AUSGABE = new ArrayList<>();

        static {
            KATEGORIE_AUSGABE.add(new SelectOption("miete", "Miete"));
            KATEGORIE_AUSGABE.add(new SelectOption("strom", "Strom"));
            KATEGORIE_AUSGABE.add(new SelectOption("lebensmittel", "Lebensmittel (Einkauf)"));
            KATEGORIE_AUSGABE.add(new SelectOption("essenUnterwegs", "Essen (Unterwegs)"));
            KATEGORIE_AUSGABE.add(new SelectOption("transportOeffentliche", "Transport Öffentliche"));
            KATEGORIE_AUSGABE.add(new SelectOption("transportKfz", "Transport Kfz"));
            KATEGORIE_AUSGABE.add(new SelectOption("internet", "Internet"));
            KATEGORIE_AUSGABE.add(new SelectOption("mobilesInternet", "Mobiles Internet"));
            KATEGORIE_AUSGABE.add(new SelectOption("sport", "Sport"));
            KATEGORIE_AUSGABE.add(new SelectOption("kleidung", "Kleidung"));
            KATEGORIE_AUSGABE.add(new SelectOption("wohnungHaushalt", "Wohnung/Haushalt"));
            KATEGORIE_AUSGABE.add(new SelectOption("sonstiges", "Sonstiges"));
        }

        public static String getKategorieLabel(String value) {
            for(SelectOption kategorie : KATEGORIE_AUSGABE) {
                if(kategorie.value.equals(value)) {
                    return kategorie.label;
                }
            }
            return null;
        }
    }
}
