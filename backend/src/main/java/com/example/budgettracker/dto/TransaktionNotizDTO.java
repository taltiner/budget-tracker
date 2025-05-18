package com.example.budgettracker.dto;

import com.example.budgettracker.model.EingabeArt;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransaktionNotizDTO extends TransaktionBaseDTO {
    private String notiz;
}
