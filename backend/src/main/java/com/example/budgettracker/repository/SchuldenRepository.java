package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.model.Schulden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SchuldenRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(SchuldenRepository.class);

    public SchuldenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Schulden save(Schulden schulden) {
        String sql = "INSERT INTO SCHULDEN (SCHULDEN_BEZEICHNUNG, HOEHE, WAEHRUNG) VALUES(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, schulden.getSchuldenBezeichnung());
                ps.setString(2, schulden.getSchuldenHoehe().getHoehe());
                ps.setString(3, schulden.getSchuldenHoehe().getWaehrung());

                return ps;
            }, keyHolder);
        } catch(Exception e) {
            log.error("Fehler beim Speichern der Schulden: {}", schulden, e);
            throw new TransaktionVerarbeitenFehlgeschlagenException(e);
        }


        if (keyHolder.getKey() != null) {
            schulden.setId(keyHolder.getKey().longValue());
        }

        return schulden;
    }
}
