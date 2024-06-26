package com.authservice.authservice.services;

import java.util.Optional;

import com.authservice.authservice.dtos.RegisterDto;
import com.authservice.authservice.dtos.UserReturnsDto;
import com.authservice.authservice.models.UserEntities;

public interface UserService {
    UserReturnsDto createUser(RegisterDto dto);
    Optional<UserEntities> findByEmail(String email);
    Optional<UserEntities> findByUsername(String username);
}
