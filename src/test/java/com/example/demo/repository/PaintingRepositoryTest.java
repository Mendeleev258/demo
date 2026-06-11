package com.example.demo.repository;

import com.example.demo.entity.Painting;
import com.example.demo.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaintingRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private PaintingRepository paintingRepository;

    @Test
    void findById_shouldReturnPainting_whenExists() {

        Painting painting = mock(Painting.class);

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of(painting));

        Optional<Painting> result = paintingRepository.findById(1);

        assertTrue(result.isPresent());
        assertEquals(painting, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of());

        Optional<Painting> result = paintingRepository.findById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowException_whenMoreThanOneFound() {

        Painting p1 = mock(Painting.class);
        Painting p2 = mock(Painting.class);

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of(p1, p2));

        assertThrows(
                ValidationException.class,
                () -> paintingRepository.findById(1)
        );
    }

    @Test
    void create_shouldReturnTrue_whenInserted() {

        Painting painting = new Painting(1,"Test", "Style", 6767, 0, UUID.randomUUID());

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        )).thenReturn(1);

        Boolean result = paintingRepository.create(painting);

        assertTrue(result);
    }

    @Test
    void update_shouldReturnTrue_whenUpdated() {

        Painting painting = new Painting(1,"Test", "Style", 6767, 0, UUID.randomUUID());

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        )).thenReturn(1);

        Boolean result = paintingRepository.update(painting);

        assertTrue(result);
    }

    @Test
    void deleteById_shouldReturnTrue_whenDeleted() {

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        )).thenReturn(1);

        Boolean result = paintingRepository.deleteById(1);

        assertTrue(result);
    }
}