package com.example.budgettracker.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("TRANSAKTION_NOTIZ")
public class TransaktionNotiz extends Transaktion {
    @Id
    private Long id;
    private Long userId;
    private String notiz;
}
