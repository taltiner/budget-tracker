package com.example.budgettracker.dto;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransaktionEinnahmeDTO extends TransaktionBaseDTO {
    private Geldbetrag betragEinnahme;
}
