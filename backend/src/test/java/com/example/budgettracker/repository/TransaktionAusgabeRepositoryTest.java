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
        TransaktionAusgabe ausgabe = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("januar")
                .kategorie("miete")
                .betragAusgabe(new Geldbetrag("750", "€"))
                .istSchulden(false)
                .build();

        // when
        TransaktionAusgabe result = underTest.save(ausgabe);

        // then
        assertEquals(EingabeArt.AUSGABE, result.getTransaktionsArt());
        assertEquals("miete", result.getKategorie());
        assertEquals(null, result.getBenutzerdefinierteKategorie());
        assertEquals("750", result.getBetragAusgabe().getHoehe());
        assertEquals("2025", result.getJahrTransaktion());
        assertEquals("januar", result.getMonatTransaktion());
    }

    @Test
    void update() {
        // given
        TransaktionAusgabe ausgabe1 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("januar")
                .kategorie("miete")
                .betragAusgabe(new Geldbetrag("800", "€"))
                .istSchulden(false)
                .build();

        TransaktionAusgabe ausgabe2 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("januar")
                .kategorie("strom")
                .betragAusgabe(new Geldbetrag("60", "€"))
                .istSchulden(false)
                .build();

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
        assertEquals(null, result.get(1).getBenutzerdefinierteKategorie());
        assertEquals("60", result.get(1).getBetragAusgabe().getHoehe());
        assertEquals("2025", result.get(1).getJahrTransaktion());
        assertEquals("januar", result.get(1).getMonatTransaktion());
    }

    @Test
    void delete() {
        // given
        TransaktionAusgabe ausgabe1 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("februar")
                .kategorie("lebensmittel")
                .betragAusgabe(new Geldbetrag("300", "€"))
                .istSchulden(false)
                .build();

        TransaktionAusgabe ausgabe2 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("april")
                .kategorie("internet")
                .betragAusgabe(new Geldbetrag("30", "€"))
                .istSchulden(false)
                .build();

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
        TransaktionAusgabe ausgabe1 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("februar")
                .kategorie("lebensmittel")
                .betragAusgabe(new Geldbetrag("300", "€"))
                .istSchulden(false)
                .build();

        TransaktionAusgabe ausgabe2 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("april")
                .kategorie("internet")
                .betragAusgabe(new Geldbetrag("30", "€"))
                .istSchulden(false)
                .build();

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
        TransaktionAusgabe ausgabe1 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("februar")
                .kategorie("lebensmittel")
                .betragAusgabe(new Geldbetrag("300", "€"))
                .istSchulden(false)
                .build();

        TransaktionAusgabe ausgabe2 = TransaktionAusgabe.builder()
                .transaktionsArt(EingabeArt.AUSGABE)
                .jahrTransaktion("2025")
                .monatTransaktion("april")
                .kategorie("internet")
                .betragAusgabe(new Geldbetrag("30", "€"))
                .istSchulden(false)
                .build();

        // when
        underTest.save(ausgabe1);
        underTest.save(ausgabe2);
        underTest.deleteAll();
        List<TransaktionAusgabe> result = underTest.findAll();

        // then
        assertEquals(0, result.size());
    }
}