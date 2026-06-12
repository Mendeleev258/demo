package com.example.demo.repository;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    String login = "ann";
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @InjectMocks
    private UserRepository userRepository;

    @Test
    void findByLogin_shouldReturnUser_whenUserExists() {

        User user = mock(User.class);

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of(user));

        Optional<User> result = userRepository.findByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findByLogin_shouldThrowException_whenMoreThanOneUserFound() {

        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of(user1, user2));

        assertThrows(
                ValidationException.class,
                () -> userRepository.findByLogin(login)
        );
    }

    @Test
    void findByLogin_shouldReturnEmpty_whenUserNotExists() {

        when(namedParameterJdbcTemplate.query(
                anyString(),
                anyMap(),
                any(RowMapper.class)
        )).thenReturn(List.of());

        Optional<User> result = userRepository.findByLogin(login);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldReturnTrue_whenOneRowInserted() {

        User user = new User(UUID.randomUUID(), login, login, login, Role.USER);

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        )).thenReturn(1);

        Boolean result = userRepository.create(user);

        assertTrue(result);
    }

    @Test
    void update_shouldReturnTrue_whenOneRowUpdated() {

        User user = new User(UUID.randomUUID(), login, login, login, Role.USER);

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        )).thenReturn(1);

        Boolean result = userRepository.update(user);

        assertTrue(result);
    }

    @Test
    void delete_shouldThrowException_whenUserNotFound() {

        when(namedParameterJdbcTemplate.update(
                anyString(),
                anyMap()
        ))
                .thenReturn(1) // удаляем все картинки этого пользователя
                .thenReturn(0); // удаляем пользоателя

        assertThrows(
                ValidationException.class,
                () -> userRepository.delete(UUID.randomUUID())
        );
    }
}
