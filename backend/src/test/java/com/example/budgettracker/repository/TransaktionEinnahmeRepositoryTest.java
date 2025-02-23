package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionEinnahme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransaktionEinnahmeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private TransaktionEinnahmeRepository underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sollteEinnahmeSpeichern() {
        // given
        TransaktionEinnahme einnahme = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "januar",
                new Geldbetrag("2500", "€"));

        // when
        TransaktionEinnahme result = underTest.save(einnahme);

        // then
        assertEquals(EingabeArt.EINNAHME, result.getTransaktionsArt());
        assertEquals("2024", result.getJahrTransaktion());
        assertEquals("januar", result.getMonatTransaktion());
        assertEquals("2500", result.getBetragEinnahme().getHoehe());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
    }

    @Test
    void deleteAll() {
    }
}