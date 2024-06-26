package com.authservice.authservice.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.authservice.authservice.dtos.TokensDto;
import com.authservice.authservice.models.UserEntities;
import com.authservice.authservice.security.JwtUtils;
import com.authservice.authservice.security.UserDetailsImpl;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final Map<String, String> refreshStorange = new HashMap<>();

    @Override
    public TokensDto refresh(String token) throws AuthException {
        if (this.jwtUtils.validateRefreshToken(token)) {
            String username = this.jwtUtils.getUsernameFromJwtRefreshToken(token);
            String savedUsername = this.refreshStorange.get(token);
            if (savedUsername != null && savedUsername.equals(username)) {
                UserEntities user = this.userService.findByUsername(savedUsername)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));

                UserDetails userDetailts = UserDetailsImpl.build(user);
                String accessToken = this.jwtUtils.generateToken(userDetailts);
                String refreshToken = this.jwtUtils.generateRefreshToken(userDetailts);
                this.refreshStorange.put(refreshToken, userDetailts.getUsername());
                return new TokensDto(accessToken, refreshToken);
            }
        }

        return new TokensDto(null, null);
    }

    @Override
    public TokensDto genereateTokens(UserDetails userDetails) {
        String token = this.jwtUtils.generateToken(userDetails);
        String refreshToken = this.jwtUtils.generateRefreshToken(userDetails);
        this.refreshStorange.put(refreshToken, userDetails.getUsername());
        return new TokensDto(token, refreshToken);
    }
}
