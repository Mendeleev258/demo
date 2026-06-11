package com.example.demo.repository;

import com.example.demo.entity.Painting;
import com.example.demo.exception.ValidationException;
import com.example.demo.service.UserService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PaintingRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PaintingRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Painting> findAll() {
        String sql = "SELECT * FROM paintings";
        return namedParameterJdbcTemplate.query(sql, PAINTING_ROW_MAPPER);
    }

    public List<Painting> findAll(int page, int size) {
        String sql = "SELECT * FROM paintings ORDER BY id LIMIT :size OFFSET :offset";

        return namedParameterJdbcTemplate.query(sql, Map.of("size", size, "offset", page * size),
                PAINTING_ROW_MAPPER);
    }

    public Optional<Painting> findById(Integer id) {
        String sql = "SELECT * FROM paintings WHERE id = :id";
        List<Painting> paintings = namedParameterJdbcTemplate.query(sql, Map.of("id", id), PAINTING_ROW_MAPPER);
        if (paintings.size() > 1) {
            throw new ValidationException("More than one painting with id " + id);
        }
        return paintings.stream().findFirst();
    }

    public Boolean create(Painting painting) {
        String sql = "INSERT INTO paintings (title, style, year_created, version, user_id )" +
                "VALUES(:title, :style, :yearCreated, :version, :userId)";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                        "title", painting.getTitle(),
                        "style", painting.getStyle(),
                        "yearCreated", painting.getYearCreated(),
                        "version", painting.getVersion(),
                        "userId", painting.getUserId()
                )
        ) == 1;
    }

    public List<Painting> findByTitle(String title) {
        String sql = "SELECT * FROM paintings WHERE LOWER(title) LIKE LOWER(:title)";
        return namedParameterJdbcTemplate.query(sql, Map.of("title", "%" + title + "%"), PAINTING_ROW_MAPPER);
    }

    public List<Painting> findByUserId(UUID userId) {
        String sql = "SELECT * FROM paintings WHERE user_id = :userId";
        return namedParameterJdbcTemplate.query(sql, Map.of("userId", userId), PAINTING_ROW_MAPPER);
    }

    public Boolean update(Painting painting) {
        String sql = "UPDATE paintings SET title = :title, style = :style, " +
                "year_created = :yearCreated, version = version + 1" +
                " WHERE id = :id AND version = :version";
        return namedParameterJdbcTemplate.update(sql, Map.of(
                        "id", painting.getId(),
                        "title", painting.getTitle(),
                        "style", painting.getStyle(),
                        "yearCreated", painting.getYearCreated(),
                        "version", painting.getVersion()
                )
        ) == 1;
    }

    public Boolean deleteById(Integer id) {
        String sql = "DELETE FROM paintings WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Map.of("id", id)) == 1;
    }

    private static final RowMapper<Painting> PAINTING_ROW_MAPPER = (rs, rowNum) -> new Painting(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("style"),
            rs.getInt("year_created"),
            rs.getInt("version"),
            rs.getObject("user_id", UUID.class)
    );
}
