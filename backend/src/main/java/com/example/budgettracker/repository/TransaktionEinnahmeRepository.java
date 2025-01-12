package com.example.budgettracker.repository;

import com.example.budgettracker.model.EingabeArt;
import com.example.budgettracker.model.Geldbetrag;
import com.example.budgettracker.model.TransaktionEinnahme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TransaktionEinnahmeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransaktionEinnahmeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TransaktionEinnahme save(TransaktionEinnahme einnahme) {
        String sql = "INSERT INTO TRANSAKTION_EINNAHME (TRANSAKTIONS_ART, JAHR_TRANSAKTION, MONAT_TRANSAKTION, HOEHE, WAEHRUNG) " +
                     "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, einnahme.getTransaktionsArt().toString());
            ps.setString(2, einnahme.getJahrTransaktion());
            ps.setString(3, einnahme.getMonatTransaktion());
            ps.setString(4, einnahme.getBetragEinnahme().getHoehe());
            ps.setString(5, einnahme.getBetragEinnahme().getWaehrung());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            einnahme.setId(keyHolder.getKey().longValue());
        }

        return einnahme;
    }

    public List<TransaktionEinnahme> findAll() {
        String sql = "SELECT * FROM TRANSAKTION_EINNAHME";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EingabeArt transaktionArt = EingabeArt.valueOf(rs.getString("TRANSAKTIONS_ART"));
            String hoehe = rs.getString("HOEHE");
            String waehrung = rs.getString("WAEHRUNG");
            Geldbetrag betragEinnahme = new Geldbetrag(hoehe, waehrung);

            TransaktionEinnahme einnahme = new TransaktionEinnahme(
                    transaktionArt,
                    rs.getString("JAHR_TRANSAKTINO"),
                    rs.getString("MONAT_TRANSAKTION"),
                    betragEinnahme
            );
            return einnahme;
        });
    }

    public void deleteAll() {
        String sql = "DELETE FROM TRANSAKTION_EINNAHME";
        jdbcTemplate.update(sql);
    }
}
