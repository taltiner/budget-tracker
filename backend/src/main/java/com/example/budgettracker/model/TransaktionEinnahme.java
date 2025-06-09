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
@Table(name = "TRANSAKTION_EINNAHME")
public class TransaktionEinnahme extends Transaktion {
    @Id
    private Long id;
    private Long userId;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
    private Geldbetrag betragEinnahme;
}
