package com.example.budgettracker.dto;
import com.example.budgettracker.model.Geldbetrag;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransaktionAusgabeDTO extends TransaktionBaseDTO{
    private String kategorie;
    private String benutzerdefinierteKategorie;
    private Geldbetrag betragAusgabe;
    private boolean istSchulden;
}
