package com.authservice.authservice.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    @Value("${app.jwt.token.expiration:7000}")
    private Long jwtTokenExpiration;
    @Value("${app.jwt.token.sign}") 
    private String tokenSign;
    @Value("${app.jwt.refresh-token.expiration:7000}")
    private Long jwtRefreshTokenExpiration;
    @Value("${app.jwt.refresh-token.sign}") 
    private String refreshTokenSign;
    
    public String generateToken(UserDetails userDetails) { 
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof UserDetailsImpl customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("username", customUserDetails.getUsername());
            claims.put("email", customUserDetails.getEmail());
            claims.put("roles", customUserDetails.getAuthorities());
        }

        return generateToken(claims, userDetails);
    }

    private String generateToken(
        Map<String, Object> claims, UserDetails userDetails
    ) {
        return Jwts.builder().addClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.jwtTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, this.tokenSign).compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + this.jwtRefreshTokenExpiration))
            .signWith(SignatureAlgorithm.HS256, this.refreshTokenSign).compact();
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = this.getClaims(token, this.tokenSign);
        return claims.getSubject();
    }

    public String getUsernameFromJwtRefreshToken(String token) {
        Claims claims = this.getClaims(token, this.refreshTokenSign);
        return claims.getSubject();
    }

    public boolean validateAccessToken(String token) {
        return this.validateToken(token, this.tokenSign);
    }

    public boolean validateRefreshToken(String token) {
        return this.validateToken(token, this.refreshTokenSign);
    }

    private boolean validateToken(String token, String secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt was expired");
        }
    }

    public List<String> refreshToken(UserDetails userDetails) {
        String token = this.generateToken(userDetails);
        String refreshToken = this.generateRefreshToken(userDetails);

        return List.of(token, refreshToken);
    }
}
