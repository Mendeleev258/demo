package com.example.demo.response;

import com.example.demo.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLogin()
        );
    }
}
