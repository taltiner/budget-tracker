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

    public class MonatAuswahl {
        private static final List<SelectOption> TRANSAKTION_MONAT = new ArrayList<>();

        static {
            TRANSAKTION_MONAT.add(new SelectOption("gesamt", "Gesamt"));
            TRANSAKTION_MONAT.add(new SelectOption("januar", "Januar"));
            TRANSAKTION_MONAT.add(new SelectOption("februar", "Februar"));
            TRANSAKTION_MONAT.add(new SelectOption("märz", "März"));
            TRANSAKTION_MONAT.add(new SelectOption("april", "April"));
            TRANSAKTION_MONAT.add(new SelectOption("mai", "Mai"));
            TRANSAKTION_MONAT.add(new SelectOption("juni", "Juni"));
            TRANSAKTION_MONAT.add(new SelectOption("juli", "Juli"));
            TRANSAKTION_MONAT.add(new SelectOption("august", "August"));
            TRANSAKTION_MONAT.add(new SelectOption("september", "September"));
            TRANSAKTION_MONAT.add(new SelectOption("oktober", "Oktober"));
            TRANSAKTION_MONAT.add(new SelectOption("november", "November"));
            TRANSAKTION_MONAT.add(new SelectOption("dezember", "Dezember"));
        }

        public static String getMonatLabel(String value) {
            for(SelectOption monat : TRANSAKTION_MONAT) {
                if(monat.value.equals(value)) {
                    return monat.label;
                }
            }
            return null;
        }

        public static String getMonatValue(String label) {
            for(SelectOption monat : TRANSAKTION_MONAT) {
                if (monat.label.equals(label)) {
                    return monat.value;
                }
            }
            return null;
        }
    }
}
