package com.rev.app.mapper;

import com.rev.app.dto.RegisterRequest;
import com.rev.app.entity.User;

public class UserMapper {

    public static User toEntity(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        return user;
    }
}