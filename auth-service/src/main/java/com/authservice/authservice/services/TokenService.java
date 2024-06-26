package com.authservice.authservice.services;

import org.springframework.security.core.userdetails.UserDetails;

import com.authservice.authservice.dtos.TokensDto;

import jakarta.security.auth.message.AuthException;

public interface TokenService {
    TokensDto refresh(String token) throws AuthException;
    TokensDto genereateTokens(UserDetails userDetails);
}
