package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionEinnahme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransaktionEinnahmeRepositoryTest {

    @Autowired
    private TransaktionEinnahmeRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
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
        // given
        TransaktionEinnahme einnahme = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "januar",
                new Geldbetrag("2500", "€"));

        underTest.save(einnahme);

        // when
        einnahme.setBetragEinnahme(new Geldbetrag("2700", "€"));
        einnahme.setMonatTransaktion("februar");
        TransaktionEinnahme result = underTest.update(einnahme);

        // then
        assertEquals(EingabeArt.EINNAHME, result.getTransaktionsArt());
        assertEquals("2024", result.getJahrTransaktion());
        assertEquals("februar", result.getMonatTransaktion());
        assertEquals("2700", result.getBetragEinnahme().getHoehe());
    }

    @Test
    void delete() {
        // given
        TransaktionEinnahme einnahme1 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "januar",
                new Geldbetrag("1500", "€"));

        TransaktionEinnahme einnahme2 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "februar",
                new Geldbetrag("1200", "€"));

        underTest.save(einnahme1);
        underTest.save(einnahme2);

        // when
        underTest.delete("januar", "2024");
        List<TransaktionEinnahme> result = underTest.findAll();

        // then
        assertEquals(1, result.size());
        assertEquals("februar", result.get(0).getMonatTransaktion());
    }

    @Test
    void findAll() {
        // given
        TransaktionEinnahme einnahme1 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "januar",
                new Geldbetrag("3500", "€"));

        TransaktionEinnahme einnahme2 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "februar",
                new Geldbetrag("3200", "€"));

        underTest.save(einnahme1);
        underTest.save(einnahme2);

        // when
        List<TransaktionEinnahme> result = underTest.findAll();

        // then
        assertEquals(2, result.size());
    }

    @Test
    void deleteAll() {
        // given
        TransaktionEinnahme einnahme1 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "januar",
                new Geldbetrag("2500", "€"));

        TransaktionEinnahme einnahme2 = new TransaktionEinnahme(
                EingabeArt.EINNAHME,
                "2024",
                "februar",
                new Geldbetrag("1800", "€"));

        underTest.save(einnahme1);
        underTest.save(einnahme2);

        // when
        underTest.deleteAll();
        List<TransaktionEinnahme> result = underTest.findAll();

        // then
        assertEquals(0, result.size());
    }
}