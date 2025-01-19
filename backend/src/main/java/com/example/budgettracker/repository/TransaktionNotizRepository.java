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
            ps.setString(1, notiz.getTransaktionsArt().toString());
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

    public List<TransaktionNotiz> findAll() {
        String sql = "SELECT * FROM TRANSAKTION_NOTIZ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            EingabeArt transaktionsArt = EingabeArt.valueOf(rs.getString("TRANSAKTIONS_ART"));
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
