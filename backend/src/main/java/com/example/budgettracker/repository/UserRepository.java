package com.example.budgettracker.repository;

import com.example.budgettracker.exception.TransaktionVerarbeitenFehlgeschlagenException;
import com.example.budgettracker.exception.UserException;
import com.example.budgettracker.model.Rolle;
import com.example.budgettracker.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByEmail(String email) {
        log.error("SQL SQL SQL");
        String sql = "SELECT * FROM _USER WHERE EMAIL = ?";

        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> {
            Long id = rs.getLong("ID");
            String vorname = rs.getString("VORNAME");
            String nachname = rs.getString("NACHNAME");
            String emailUser = rs.getString("EMAIL");
            String password = rs.getString("PASSWORD");
            String rolle = rs.getString("ROLLE");

            User user = User.builder()
                    .vorname(vorname)
                    .nachname(nachname)
                    .email(emailUser)
                    .password(password)
                    .rolle(Rolle.valueOf(rolle))
                    .build();

            user.setId(id);
            return user;
        });

        return users.stream().findFirst();
    }

    public User save(User user) {
        String sql = "INSERT INTO _USER (VORNAME, NACHNAME, EMAIL, ROLLE, PASSWORD) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setString(1, user.getVorname());
                ps.setString(2, user.getNachname());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getRolle().toString());
                ps.setString(5, user.getPassword());

                return ps;
            }, keyHolder);
        } catch(Exception e) {
            log.error("Fehler beim Anlegen des Users: {}", user, e);
            throw new UserException(String.format("Fehler beim Anlegen des Users: ", user));
        }


        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }

        return user;
    }
}
