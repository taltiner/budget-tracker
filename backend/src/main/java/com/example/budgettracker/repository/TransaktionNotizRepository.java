package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.TransaktionNotiz;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
@Repository
public class TransaktionNotizRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransaktionNotizRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionNotiz save(TransaktionNotiz notiz) {
        String sql = "INSERT INTO TRANSAKTION_NOTIZ (TRANSAKTIONS_ART, JAHR_TRANSAKTION, MONAT_TRANSAKTION, NOTIZ) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, notiz.getTransaktionsArt().getValue());
            ps.setString(2, notiz.getJahrTransaktion());
            ps.setString(3, notiz.getMonatTransaktion());
            ps.setString(4, notiz.getNotiz());
            return ps;
        }, keyHolder);

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
        String sql = "DELETE FROM TRANSAKTION_NOTIZ WHERE MONAT_TRANSAKTION = ? AND JAHR_TRANSAKTION = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, monat);
            ps.setString(2, jahr);
            return ps;
        });
    }

    public List<TransaktionNotiz> findAll() {
        String sql = "SELECT * FROM TRANSAKTION_NOTIZ";

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

            TransaktionNotiz notizSaved = new TransaktionNotiz(
                transaktionsArt,
                jahrTransaktion,
                monatTransaktion,
                notiz
            );
            notizSaved.setId(id);
            return notizSaved;
        });
    }

    public void deleteAll(){
        String sql = "DELETE FROM TRANSAKTION_NOTIZ";
        jdbcTemplate.update(sql);
    }
}
