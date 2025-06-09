package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionLoeschenFehlgeschlagenException;
import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SchuldenRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(SchuldenRepository.class);

    public SchuldenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Schulden save(Schulden schulden) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "INSERT INTO SCHULDEN (SCHULDEN_BEZEICHNUNG, HOEHE, WAEHRUNG, USER_ID) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, schulden.getSchuldenBezeichnung());
                ps.setString(2, schulden.getSchuldenHoehe().getHoehe());
                ps.setString(3, schulden.getSchuldenHoehe().getWaehrung());
                ps.setLong(4, user.getId());

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


    public List<Schulden> update(List<Schulden> schulden) {
        deleteAll();

        return schulden.stream()
                .map(schuld -> {

                    return save(schuld);
                })
                .collect(Collectors.toList());
    }

    public void delete(Schulden schulden) {
        String schuldenBezeichnung = schulden.getSchuldenBezeichnung();

        String sql = "DELETE FROM SCHULDEN WHERE SCHULDEN_BEZEICHNUNG = ?";
        try {
            int geloeschteZeilen = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, schuldenBezeichnung);
                return ps;
            });
        } catch(Exception e) {
            throw new TransaktionLoeschenFehlgeschlagenException(String.format("Die Schuld mit der Bezeichnung: %s konnte nicht gefunden werden. Es wurden keine Schulden gelöscht.", schuldenBezeichnung), e);
        }
    }

    public void deleteAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "DELETE FROM SCHULDEN WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    public List<Schulden> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();

        String sql = "SELECT * FROM SCHULDEN WHERE USER_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            String schuldenBezeichnung = rs.getString("SCHULDEN_BEZEICHNUNG");
            String hoehe = rs.getString("HOEHE");
            String waehrung = rs.getString("WAEHRUNG");
            Geldbetrag schuldenHoehe = new Geldbetrag(hoehe, waehrung);

            Schulden schulden = Schulden.builder()
                    .schuldenBezeichnung(schuldenBezeichnung)
                    .schuldenHoehe(schuldenHoehe)
                    .build();

            schulden.setId(id);
            return schulden;
        }, userId);
    }

}
