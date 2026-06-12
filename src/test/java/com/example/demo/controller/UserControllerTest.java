package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.response.UserResponse;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private final UUID id = UUID.randomUUID();
    private final String login = "ann";

    @Test
    void findAll_shouldReturnUsers() {

        List<UserResponse> expected = List.of(new UserResponse(UUID.randomUUID(), login, login));

        when(userService.findAll()).thenReturn(expected);

        List<UserResponse> result = userController.findAll();

        assertThat(result).isEqualTo(expected);
        verify(userService).findAll();
    }

    @Test
    void findByLogin_shouldReturnUser() {

        UserResponse response = new UserResponse(UUID.randomUUID(), login, login);

        when(userService.findByLogin(login)).thenReturn(Optional.of(response));

        Optional<UserResponse> result = userController.findByLogin(login);

        assertThat(result).isPresent();
        assertThat(result.get().login()).isEqualTo(login);

        verify(userService).findByLogin(login);
    }

    @Test
    void createUser_shouldCallService() {

        CreateUserRequest request = new CreateUserRequest(login, login, login);

        User user = new User(id, "Ann", login, login, Role.USER);

        when(userService.create(request)).thenReturn(user);

        User result = userController.createUser(request);

        assertThat(result).isEqualTo(user);
        verify(userService).create(request);
    }

    @Test
    void updateUser_shouldCallService() {

        CreateUserRequest request = new CreateUserRequest(login, login, login);

        User user = new User(id, "Ann", login, login, Role.USER);

        when(userService.update(request, authentication)).thenReturn(user);

        User result = userController.updateUser(request, authentication);

        assertThat(result).isEqualTo(user);

        verify(userService).update(request, authentication);
    }

    @Test
    void deleteUser_shouldCallService() {

        userController.deleteUser(id);

        verify(userService).delete(id);
    }
}