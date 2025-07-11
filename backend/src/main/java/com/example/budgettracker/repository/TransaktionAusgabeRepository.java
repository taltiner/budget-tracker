package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionLoeschenFehlgeschlagenException;
import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TransaktionAusgabeRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(TransaktionAusgabeRepository.class);

    public TransaktionAusgabeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionAusgabe save(TransaktionAusgabe ausgabe) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "INSERT INTO TRANSAKTION_AUSGABE (TRANSAKTIONS_ART, KATEGORIE, BENUTZERDEFINIERTE_KATEGORIE, HOEHE, WAEHRUNG, JAHR_TRANSAKTION, MONAT_TRANSAKTION, IST_SCHULDEN, USER_ID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, ausgabe.getTransaktionsArt().getValue());
                ps.setString(2, ausgabe.getKategorie());
                ps.setString(3, ausgabe.getBenutzerdefinierteKategorie());
                ps.setString(4, ausgabe.getBetragAusgabe().getHoehe());
                ps.setString(5, ausgabe.getBetragAusgabe().getWaehrung());
                ps.setString(6, ausgabe.getJahrTransaktion());
                ps.setString(7, ausgabe.getMonatTransaktion());
                ps.setBoolean(8, ausgabe.isIstSchulden());
                ps.setLong(9, user.getId());


                return ps;
            }, keyHolder);
        } catch(Exception e) {
            log.error("Fehler beim Speichern der Transaktion: {}", ausgabe, e);
            throw new TransaktionVerarbeitenFehlgeschlagenException(e);
        }


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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "DELETE FROM TRANSAKTION_AUSGABE WHERE MONAT_TRANSAKTION = ? AND JAHR_TRANSAKTION = ? AND USER_ID = ?";

        try {
            int geloeschteZeilen = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, monat);
                ps.setString(2, jahr);
                ps.setLong(3, user.getId());
                return ps;
            });
        } catch(Exception e) {
            throw new TransaktionLoeschenFehlgeschlagenException(String.format(
                    "Die Transaktion für den Monat: %s und Jahr: %s konnte nicht gefunden werden. Es wurden keine Transaktionen gelöscht." , monat, jahr), e);
        }
    }


    public List<TransaktionAusgabe> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "SELECT * FROM TRANSAKTION_AUSGABE WHERE USER_ID = ?";

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
            boolean istSchulden = rs.getBoolean("IST_SCHULDEN");

            TransaktionAusgabe ausgabe = TransaktionAusgabe.builder()
                    .transaktionsArt(transaktionsArt)
                    .jahrTransaktion(jahrTransaktion)
                    .monatTransaktion(monatTransaktion)
                    .kategorie(kategorie)
                    .benutzerdefinierteKategorie(benutzerdefinierteKategorie)
                    .betragAusgabe(betragAusgabe)
                    .istSchulden(istSchulden)
                    .build();

            ausgabe.setId(id);
            return ausgabe;
        }, userId);
    }


    public void deleteAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "DELETE FROM TRANSAKTION_AUSGABE";
        jdbcTemplate.update(sql, userId);
    }
}
