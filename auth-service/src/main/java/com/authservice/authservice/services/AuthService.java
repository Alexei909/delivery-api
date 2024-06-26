package com.authservice.authservice.services;

import com.authservice.authservice.dtos.LoginDto;
import com.authservice.authservice.dtos.TokensDto;

public interface AuthService {
    TokensDto login(LoginDto dto);
    
}
