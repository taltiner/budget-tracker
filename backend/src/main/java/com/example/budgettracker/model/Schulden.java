package com.example.budgettracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("SCHULDEN")
public class Schulden {
    @Id
    private Long id;
    private Long userId;
    private String schuldenBezeichnung;
    private Geldbetrag schuldenHoehe;
}
