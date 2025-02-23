package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.TransaktionNotiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransaktionNotizRepositoryTest {

    @Autowired
    private TransaktionNotizRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void save() {
        // given
        String notizText = "Das ist eine Notiz";
        TransaktionNotiz notiz = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "januar",
                notizText
        );

        // when
        TransaktionNotiz result = underTest.save(notiz);

        // then
        assertEquals(EingabeArt.NOTIZ, result.getTransaktionsArt());
        assertEquals("2025", result.getJahrTransaktion());
        assertEquals("januar", result.getMonatTransaktion());
        assertEquals(notizText, result.getNotiz());
    }

    @Test
    void update() {
        // given
        String notizText = "Das ist eine Notiz";
        TransaktionNotiz notiz = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "januar",
                notizText
        );

        // when
        underTest.save(notiz);
        notiz.setNotiz("TEST");
        notiz.setMonatTransaktion("februar");
        TransaktionNotiz result = underTest.update(notiz);

        // then
        assertEquals("TEST", result.getNotiz());
        assertEquals("februar", result.getMonatTransaktion());
        assertEquals("2025", result.getJahrTransaktion());
    }

    @Test
    void delete() {
        // given
        TransaktionNotiz notiz1 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "januar",
                "Test 1"
        );
        TransaktionNotiz notiz2 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "april",
                "Test 2"
        );
        underTest.save(notiz1);
        underTest.save(notiz2);

        // when
        underTest.delete("januar", "2025");
        List<TransaktionNotiz> result = underTest.findAll();

        // then
        assertEquals(1, result.size());
        assertEquals(EingabeArt.NOTIZ, result.get(0).getTransaktionsArt());
        assertEquals("2025", result.get(0).getJahrTransaktion());
        assertEquals("april", result.get(0).getMonatTransaktion());
        assertEquals("Test 2", result.get(0).getNotiz());
    }

    @Test
    void findAll() {
        // given
        TransaktionNotiz notiz1 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "januar",
                "Test 1"
        );
        TransaktionNotiz notiz2 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "april",
                "Test 2"
        );
        underTest.save(notiz1);
        underTest.save(notiz2);

        // when
        List<TransaktionNotiz> result = underTest.findAll();

        // then
        assertEquals(2, result.size());
    }

    @Test
    void deleteAll() {
        // given
        TransaktionNotiz notiz1 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "januar",
                "Test 1"
        );
        TransaktionNotiz notiz2 = new TransaktionNotiz(
                EingabeArt.NOTIZ,
                "2025",
                "april",
                "Test 2"
        );
        underTest.save(notiz1);
        underTest.save(notiz2);

        // when
        underTest.deleteAll();
        List<TransaktionNotiz> result = underTest.findAll();

        // then
        assertEquals(0, result.size());
    }
}