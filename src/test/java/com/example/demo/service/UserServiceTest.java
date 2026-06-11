package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    String login = "ann";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    void findByLogin_shouldReturnUserResponse_whenUserExists() {

        User user = mock(User.class);
        when(user.getLogin()).thenReturn(login);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(user));

        Optional<UserResponse> result = userService.findByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(login, result.get().login());
    }

    @Test
    void findByLogin_shouldReturnEmpty_whenUserNotExists() {

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.empty());

        Optional<UserResponse> result =
                userService.findByLogin(login);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldThrowException_whenUserAlreadyExists() {

        CreateUserRequest request =
                mock(CreateUserRequest.class);

        User user = mock(User.class);

        when(request.login()).thenReturn(login);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(user));

        assertThrows(
                ValidationException.class,
                () -> userService.create(request)
        );
    }

    @Test
    void create_shouldCreateUser_whenLoginIsFree() {

        CreateUserRequest request = new CreateUserRequest(login, login, login);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(login))
                .thenReturn("encodedPassword");

        when(userRepository.create(any(User.class)))
                .thenReturn(true);

        User result = userService.create(request);

        assertEquals(login, result.getName());
        assertEquals(login, result.getLogin());
        assertEquals(Role.USER, result.getRole());

        verify(userRepository).lockOnValue(login);
        verify(userRepository).create(any(User.class));
    }

    @Test
    void create_shouldThrowException_whenInsertFails() {

        CreateUserRequest request = new CreateUserRequest(login, login, login);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(login))
                .thenReturn("encoded");

        when(userRepository.create(any(User.class)))
                .thenReturn(false);

        assertThrows(ValidationException.class,
                () -> userService.create(request));
    }

    @Test
    void delete_shouldCallRepository() {

        UUID id = UUID.randomUUID();

        userService.delete(id);

        verify(userRepository).delete(id);
    }

    @Test
    void update_shouldUpdateUser_whenValid() {

        CreateUserRequest request = new CreateUserRequest(login, "newLogin", login);

        User oldUser = new User(UUID.randomUUID(),login, login, login, Role.USER);

        when(authentication.getName()).thenReturn(login);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(oldUser));

        when(userRepository.findByLogin("newLogin"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(login))
                .thenReturn("encoded");

        when(userRepository.update(any(User.class)))
                .thenReturn(true);

        User result = userService.update(request, authentication);

        assertEquals("newLogin", result.getLogin());

        verify(userRepository).update(any(User.class));
    }
}