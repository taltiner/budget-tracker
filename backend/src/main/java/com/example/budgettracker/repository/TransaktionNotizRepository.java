package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionLoeschenFehlgeschlagenException;
import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.TransaktionNotiz;
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
public class TransaktionNotizRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(TransaktionNotizRepository.class);

    public TransaktionNotizRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionNotiz save(TransaktionNotiz notiz) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "INSERT INTO TRANSAKTION_NOTIZ (TRANSAKTIONS_ART, JAHR_TRANSAKTION, MONAT_TRANSAKTION, NOTIZ, USER_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, notiz.getTransaktionsArt().getValue());
                ps.setString(2, notiz.getJahrTransaktion());
                ps.setString(3, notiz.getMonatTransaktion());
                ps.setString(4, notiz.getNotiz());
                ps.setLong(5, user.getId());
                return ps;
            }, keyHolder);
        } catch(Exception e) {
            log.error("Fehler beim Speichern der Transaktion: {}", notiz, e);
            throw new TransaktionVerarbeitenFehlgeschlagenException(e);
        }


        if (keyHolder.getKey() != null) {
            notiz.setId(keyHolder.getKey().longValue());
        }

        return notiz;
    }

    public TransaktionNotiz update(TransaktionNotiz notiz) {
        delete(notiz.getMonatTransaktion(), notiz.getJahrTransaktion());
        save(notiz);

        return notiz;
    }

    public void delete(String monat, String jahr) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "DELETE FROM TRANSAKTION_NOTIZ WHERE MONAT_TRANSAKTION = ? AND JAHR_TRANSAKTION = ? AND USER_ID = ?";

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
                    "Die Notiz für den Monat: %s und Jahr: %s konnte nicht gefunden werden. Es wurden keine Notizen gelöscht." , monat, jahr));
        }
    }

    public List<TransaktionNotiz> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "SELECT * FROM TRANSAKTION_NOTIZ WHERE USER_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            String transaktionsArtString = rs.getString("TRANSAKTIONS_ART");
            EingabeArt transaktionsArt = null;
            if (transaktionsArtString != null) {
                transaktionsArt = EingabeArt.fromValue(transaktionsArtString.toLowerCase());
            }

            String jahrTransaktion = rs.getString("JAHR_TRANSAKTION");
            String monatTransaktion = rs.getString("MONAT_TRANSAKTION");
            String notiz = rs.getString("NOTIZ");

            TransaktionNotiz notizSaved = TransaktionNotiz.builder()
                    .transaktionsArt(transaktionsArt)
                    .jahrTransaktion(jahrTransaktion)
                    .monatTransaktion(monatTransaktion)
                    .notiz(notiz)
                    .build();

            notizSaved.setId(id);
            return notizSaved;
        }, userId);
    }


    public void deleteAll(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "DELETE FROM TRANSAKTION_NOTIZ";
        jdbcTemplate.update(sql, userId);
    }
}
