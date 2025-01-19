package com.example.budgettracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EingabeArt {
    EINNAHME("einnahme"),
    AUSGABE("ausgabe"),
    NOTIZ("notiz");

    private final String value;

    EingabeArt(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EingabeArt fromValue(String value) {
        for (EingabeArt art : EingabeArt.values()) {
            if (art.value.equalsIgnoreCase(value)) {
                return art;
            }
        }
        throw new IllegalArgumentException("Unbekannter Wert für EingabeArt: " + value);
    }
}

