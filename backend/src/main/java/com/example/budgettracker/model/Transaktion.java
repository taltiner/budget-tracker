package com.example.budgettracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transaktion {
    private EingabeArt transaktionsArt;
    private String jahrTransaktion;
    private String monatTransaktion;
}
