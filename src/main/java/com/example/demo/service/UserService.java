package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.response.ErrorCode;
import com.example.demo.response.UserMapper;
import com.example.demo.response.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public Optional<UserResponse> findByLogin(String login) {
        return userRepository.findByLogin(login).map(UserMapper::toResponse);
    }

    @Transactional
    public User create(CreateUserRequest request) {
        userRepository.lockOnValue(request.login());
        Optional<User> mabeUser = userRepository.findByLogin(request.login());
        if(mabeUser.isPresent()) {
            throw new ValidationException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User newUser = new User(
                UUID.randomUUID(),
                request.name(),
                request.login(),
                passwordEncoder.encode(request.passwordHash()),
                Role.USER
        );
        if(!userRepository.create(newUser)) {
            throw new ValidationException("User creation failed");
        }
        return newUser;
    }

    @Transactional
    public User update(CreateUserRequest request, Authentication auth) {
        userRepository.lockOnValue(request.login());
        User oldUser = userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));
        Optional<User> existingUser = userRepository.findByLogin(request.login());

        if (existingUser.isPresent() && !existingUser.get().getId().equals(oldUser.getId())) {
            throw new ValidationException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User updatedUser = new User(
                oldUser.getId(),
                request.name(),
                request.login(),
                passwordEncoder.encode(request.passwordHash()),
                oldUser.getRole()

        );
        if(!userRepository.update(updatedUser)) {
            throw new ValidationException("User updating failed");
        }
        return updatedUser;
    }

    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
