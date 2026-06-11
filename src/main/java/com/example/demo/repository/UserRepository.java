package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "select id, name, login, role from users";
        return namedParameterJdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    public Optional<User> findByLogin(String login) {
        String sql = "select id, name, login, password_hash, role from users where login = :login";
        List<User> users = namedParameterJdbcTemplate.query(sql, Map.of("login", login), USER_FULL_MAPPER);
        if (users.size() > 1) {
            throw new ValidationException("More than one user with the login " + login);
        }
        return users.stream().findFirst();
    }

    public void lockOnValue(Object value) {
        String sql = "SELECT pg_advisory_xact_lock(hashtext(:lock));";
        namedParameterJdbcTemplate.queryForObject(sql, Map.of("lock", value.toString()), Object.class);
    }

    public Boolean create(User user) {
        String sql = "insert into users (id, name, login, password_hash) values (:id, :name, :login, :passwordHash);";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "login", user.getLogin(),
                "passwordHash", user.getPasswordHash()
            )
        ) == 1;
    }

    public Boolean update(User user) {
        String sql = "UPDATE users " +
                "SET name = :name, " +
                "login = :login, " +
                "password_hash = :passwordHash " +
                " WHERE id = :id;";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "login", user.getLogin(),
                        "passwordHash", user.getPasswordHash()
                )
        ) == 1;
    }

    public void delete(UUID id) {
        namedParameterJdbcTemplate.update(
                "DELETE FROM paintings WHERE user_id = :id",
                Map.of("id", id)
        );

        int rows = namedParameterJdbcTemplate.update(
                "DELETE FROM users WHERE id = :id",
                Map.of("id", id)
        );

        if (rows == 0) {
            throw new ValidationException("User not found");
        }
    }

    public Optional<User> findById(UUID id) {
        String sql = "select id, name, login, password_hash, role from users where id = :id";
        return namedParameterJdbcTemplate.query( sql, Map.of("id", id), USER_FULL_MAPPER).stream().findFirst();
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getObject("id", UUID.class),
            rs.getString("name"),
            rs.getString("login"),
            null,
            Role.valueOf(rs.getString("role"))
    );

    private static final RowMapper<User> USER_FULL_MAPPER = (rs, rowNum) -> new User(
            rs.getObject("id", UUID.class),
            rs.getString("name"),
            rs.getString("login"),
            rs.getString("password_hash"),
            Role.valueOf(rs.getString("role"))
    );
}
