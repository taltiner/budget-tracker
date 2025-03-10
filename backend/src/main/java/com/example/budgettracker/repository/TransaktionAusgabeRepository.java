package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionAusgabe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TransaktionAusgabeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransaktionAusgabeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionAusgabe save(TransaktionAusgabe ausgabe) {
        String sql = "INSERT INTO TRANSAKTION_AUSGABE (TRANSAKTIONS_ART, KATEGORIE, BENUTZERDEFINIERTE_KATEGORIE, HOEHE, WAEHRUNG, JAHR_TRANSAKTION, MONAT_TRANSAKTION) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, ausgabe.getTransaktionsArt().getValue());
            ps.setString(2, ausgabe.getKategorie());
            ps.setString(3, ausgabe.getBenutzerdefinierteKategorie());
            ps.setString(4, ausgabe.getBetragAusgabe().getHoehe());
            ps.setString(5, ausgabe.getBetragAusgabe().getWaehrung());
            ps.setString(6, ausgabe.getJahrTransaktion());
            ps.setString(7, ausgabe.getMonatTransaktion());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            ausgabe.setId(keyHolder.getKey().longValue());
        }

        return ausgabe;
    }

    public List<TransaktionAusgabe> update(List<TransaktionAusgabe> ausgaben) {
        delete(ausgaben.get(0).getMonatTransaktion(), ausgaben.get(0).getJahrTransaktion());
        ausgaben.stream().forEach(ausgabe -> save(ausgabe));

        return ausgaben;
    }

    public void delete(String monat, String jahr) {
        String sql = "DELETE FROM TRANSAKTION_AUSGABE WHERE MONAT_TRANSAKTION = ? AND JAHR_TRANSAKTION = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, monat);
            ps.setString(2, jahr);
            return ps;
        });
    }


    public List<TransaktionAusgabe> findAll() {
        String sql = "SELECT * FROM TRANSAKTION_AUSGABE";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            String transaktionsArtString = rs.getString("TRANSAKTIONS_ART");
            EingabeArt transaktionsArt = null;
            if (transaktionsArtString != null) {
                transaktionsArt = EingabeArt.fromValue(transaktionsArtString.toLowerCase());
            }

            String kategorie = rs.getString("KATEGORIE");
            String benutzerdefinierteKategorie = rs.getString("BENUTZERDEFINIERTE_KATEGORIE");
            String hoehe = rs.getString("HOEHE");
            String waehrung = rs.getString("WAEHRUNG");
            Geldbetrag betragAusgabe = new Geldbetrag(hoehe, waehrung);
            String jahrTransaktion = rs.getString("JAHR_TRANSAKTION");
            String monatTransaktion = rs.getString("MONAT_TRANSAKTION");

            TransaktionAusgabe ausgabe = new TransaktionAusgabe(
                transaktionsArt,
                kategorie,
                benutzerdefinierteKategorie,
                betragAusgabe,
                jahrTransaktion,
                monatTransaktion
            );
            ausgabe.setId(id);
            return ausgabe;
        });
    }

    public void deleteAll() {
        String sql = "DELETE FROM TRANSAKTION_AUSGABE";
        jdbcTemplate.update(sql);
    }
}
