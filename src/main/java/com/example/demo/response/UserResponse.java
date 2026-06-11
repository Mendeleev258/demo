package com.example.demo.response;

import java.util.UUID;

public record UserResponse(UUID id, String name, String login) {
}
