package com.example.budgettracker.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransaktionUebersichtDTO {
    private List<TransaktionEinnahmeDTO> einnahmen;
    private List<TransaktionAusgabeDTO> ausgaben;
    private List<TransaktionNotizDTO> notizen;
}
