package com.example.authapi.dto;

import com.example.authapi.enums.Roles;

public record CreateUserDTO(

        String email,
        String password,
        Roles role
) {
}
