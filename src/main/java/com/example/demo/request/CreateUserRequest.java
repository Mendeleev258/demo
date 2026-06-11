package com.example.demo.request;

public record CreateUserRequest(String name, String login, String passwordHash) {
}
