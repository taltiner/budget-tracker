package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionLoeschenFehlgeschlagenException;
import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionEinnahme;
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
public class TransaktionEinnahmeRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(TransaktionEinnahmeRepository.class);


    public TransaktionEinnahmeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionEinnahme save(TransaktionEinnahme einnahme) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "INSERT INTO TRANSAKTION_EINNAHME (TRANSAKTIONS_ART, JAHR_TRANSAKTION, MONAT_TRANSAKTION, HOEHE, WAEHRUNG, USER_ID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, einnahme.getTransaktionsArt().getValue());
                ps.setString(2, einnahme.getJahrTransaktion());
                ps.setString(3, einnahme.getMonatTransaktion());
                ps.setString(4, einnahme.getBetragEinnahme().getHoehe());
                ps.setString(5, einnahme.getBetragEinnahme().getWaehrung());
                ps.setLong(6, user.getId());
                return ps;
            }, keyHolder);
        } catch(Exception e) {
            log.error("Fehler beim Speichern der Transaktion: {}", einnahme, e);
            throw new TransaktionVerarbeitenFehlgeschlagenException(e);
        }


        if (keyHolder.getKey() != null) {
            einnahme.setId(keyHolder.getKey().longValue());
        }

        return einnahme;
    }

    public TransaktionEinnahme update(TransaktionEinnahme einnahme) {
        delete(einnahme.getMonatTransaktion(), einnahme.getJahrTransaktion());
        save(einnahme);

        return einnahme;
    }

    public void delete(String monat, String jahr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "DELETE FROM TRANSAKTION_EINNAHME WHERE MONAT_TRANSAKTION = ? AND JAHR_TRANSAKTION = ? AND USER_ID = ?";

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

    public List<TransaktionEinnahme> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "SELECT * FROM TRANSAKTION_EINNAHME WHERE USER_ID = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            String transaktionsArtString = rs.getString("TRANSAKTIONS_ART");
            EingabeArt transaktionsArt = null;

            if (transaktionsArtString != null) {
                transaktionsArt = EingabeArt.fromValue(transaktionsArtString.toLowerCase());
            }

            String hoehe = rs.getString("HOEHE");
            String waehrung = rs.getString("WAEHRUNG");
            Geldbetrag betragEinnahme = new Geldbetrag(hoehe, waehrung);

            TransaktionEinnahme einnahme = TransaktionEinnahme.builder()
                    .transaktionsArt(transaktionsArt)
                    .jahrTransaktion(rs.getString("JAHR_TRANSAKTION"))
                    .monatTransaktion(rs.getString("MONAT_TRANSAKTION"))
                    .betragEinnahme(betragEinnahme)
                    .build();

            einnahme.setId(id);
            return einnahme;
        });
    }

    public void deleteAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "DELETE FROM TRANSAKTION_EINNAHME WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }
}
