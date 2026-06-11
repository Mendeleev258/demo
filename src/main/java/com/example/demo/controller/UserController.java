package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.request.CreateUserRequest;
import com.example.demo.response.UserResponse;
import com.example.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> findAll(){
        return userService.findAll();
    }

    @GetMapping("/search")
    public Optional<UserResponse> findByLogin(@RequestParam String login){
        return userService.findByLogin(login);
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request ){
        return userService.create(request);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody CreateUserRequest request, Authentication authentication) {
        return userService.update(request, authentication);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }
}
