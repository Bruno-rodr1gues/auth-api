package com.example.authapi.dto;

import com.example.authapi.entity.Role;
import com.example.authapi.enums.Roles;

import java.util.List;

public record RecoveryUserDTO (

        Long id,
        String email,
        List<Role> roles

) {
}