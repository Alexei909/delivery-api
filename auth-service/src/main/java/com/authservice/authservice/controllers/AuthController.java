package com.authservice.authservice.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.authservice.authservice.dtos.LoginDto;
import com.authservice.authservice.dtos.RefreshJwtRequest;
import com.authservice.authservice.dtos.RegisterDto;
import com.authservice.authservice.dtos.RoleDto;
import com.authservice.authservice.dtos.TokensDto;
import com.authservice.authservice.dtos.UserReturnsDto;
import com.authservice.authservice.models.Roles;
import com.authservice.authservice.repositories.RoleRepository;
import com.authservice.authservice.services.TokenService;
import com.authservice.authservice.services.UserService;
import com.authservice.authservice.services.AuthService;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("role")
    public void createRole(@RequestBody RoleDto dto) {
        this.roleRepository.save(new Roles(null, dto.getName()));
    }

    @GetMapping("role")
    public List<Roles> getRole() {
        return this.roleRepository.findAll();
    }

    @PostMapping("register")
    public ResponseEntity<UserReturnsDto> register(
        @Valid @RequestBody RegisterDto dto,
        BindingResult bindingResult,
        UriComponentsBuilder uriComponentsBuilder
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        } else {
            UserReturnsDto user = this.userService.createUser(dto);
            return ResponseEntity
                    .created(uriComponentsBuilder
                        .replacePath("/api/auth/user/{userId}")
                        .build(Map.of("userId", user.getId())))
                    .body(user);
        }
    }

    @PostMapping("login")
    public ResponseEntity<TokensDto> login(
        @RequestBody LoginDto dto
    ) throws Exception {
        TokensDto tokens = this.authService.login(dto);
        return ResponseEntity.ok()
                .body(tokens);
    }

    @PostMapping("refresh") 
    public ResponseEntity<TokensDto> refreshToken(
        @RequestBody RefreshJwtRequest request
    ) throws AuthException {
        TokensDto tokens = this.tokenService.refresh(request.getRefreshToken());
        return ResponseEntity.ok()
                .body(tokens);
    }

    @GetMapping("check")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok().body(null);
    }
}
