package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionAusgabe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransaktionAusgabeRepositoryTest {

    @Autowired
    private TransaktionAusgabeRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void save() {
        // given
        TransaktionAusgabe ausgabe = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "miete",
                "",
                new Geldbetrag("750", "€"),
                "2025",
                "januar"
        );

        // when
        TransaktionAusgabe result = underTest.save(ausgabe);

        // then
        assertEquals(EingabeArt.AUSGABE, result.getTransaktionsArt());
        assertEquals("miete", result.getKategorie());
        assertEquals("", result.getBenutzerdefinierteKategorie());
        assertEquals("750", result.getBetragAusgabe().getHoehe());
        assertEquals("2025", result.getJahrTransaktion());
        assertEquals("januar", result.getMonatTransaktion());
    }

    @Test
    void update() {
        // given
        TransaktionAusgabe ausgabe1 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "miete",
                "",
                new Geldbetrag("800", "€"),
                "2025",
                "januar"
        );
        TransaktionAusgabe ausgabe2 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "strom",
                "",
                new Geldbetrag("60", "€"),
                "2025",
                "januar"
        );

        // when
        underTest.save(ausgabe1);

        List<TransaktionAusgabe> ausgaben = new ArrayList<>();
        ausgabe1.setBetragAusgabe(new Geldbetrag("880", "€"));
        ausgaben.add(ausgabe1);
        ausgaben.add(ausgabe2);

        List<TransaktionAusgabe> result = underTest.update(ausgaben);

        // then
        assertEquals(2, result.size());
        assertEquals("880", result.get(0).getBetragAusgabe().getHoehe());
        assertEquals("strom", result.get(1).getKategorie());
        assertEquals("", result.get(1).getBenutzerdefinierteKategorie());
        assertEquals("60", result.get(1).getBetragAusgabe().getHoehe());
        assertEquals("2025", result.get(1).getJahrTransaktion());
        assertEquals("januar", result.get(1).getMonatTransaktion());
    }

    @Test
    void delete() {
        // given
        TransaktionAusgabe ausgabe1 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "lebensmittel",
                "",
                new Geldbetrag("300", "€"),
                "2025",
                "februar"
        );
        TransaktionAusgabe ausgabe2 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "internet",
                "",
                new Geldbetrag("30", "€"),
                "2025",
                "april"
        );

        // when
        underTest.save(ausgabe1);
        underTest.save(ausgabe2);
        underTest.delete("februar", "2025");
        List<TransaktionAusgabe> result = underTest.findAll();

        // then
        assertEquals(1, result.size());
        assertEquals("april", result.get(0).getMonatTransaktion());
    }

    @Test
    void findAll() {
        // given
        TransaktionAusgabe ausgabe1 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "lebensmittel",
                "",
                new Geldbetrag("300", "€"),
                "2025",
                "februar"
        );
        TransaktionAusgabe ausgabe2 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "internet",
                "",
                new Geldbetrag("30", "€"),
                "2025",
                "april"
        );

        // when
        underTest.save(ausgabe1);
        underTest.save(ausgabe2);
        List<TransaktionAusgabe> result = underTest.findAll();

        // then
        assertEquals(2, result.size());
    }

    @Test
    void deleteAll() {
        // given
        TransaktionAusgabe ausgabe1 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "lebensmittel",
                "",
                new Geldbetrag("300", "€"),
                "2025",
                "februar"
        );
        TransaktionAusgabe ausgabe2 = new TransaktionAusgabe(
                EingabeArt.AUSGABE,
                "internet",
                "",
                new Geldbetrag("30", "€"),
                "2025",
                "april"
        );

        // when
        underTest.save(ausgabe1);
        underTest.save(ausgabe2);
        underTest.deleteAll();
        List<TransaktionAusgabe> result = underTest.findAll();

        // then
        assertEquals(0, result.size());
    }
}