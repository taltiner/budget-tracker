package com.example.budgettracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("TRANSAKTION_AUSGABE")
public class TransaktionAusgabe extends Transaktion {
    @Id
    private Long id;
    private String kategorie;
    private String benutzerdefinierteKategorie;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragAusgabe;
    private boolean istSchulden;
 }
