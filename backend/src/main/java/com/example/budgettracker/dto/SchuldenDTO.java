package com.example.budgettracker.dto;

import com.example.budgettracker.model.Geldbetrag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchuldenDTO {
    private String schuldenBezeichnung;
    private Geldbetrag schuldenHoehe;
}
