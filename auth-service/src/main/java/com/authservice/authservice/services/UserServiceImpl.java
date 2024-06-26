package com.authservice.authservice.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.authservice.dtos.RegisterDto;
import com.authservice.authservice.dtos.UserReturnsDto;
import com.authservice.authservice.mappers.UserMapper;
import com.authservice.authservice.models.Roles;
import com.authservice.authservice.models.UserEntities;
import com.authservice.authservice.repositories.RoleRepository;
import com.authservice.authservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserReturnsDto createUser(RegisterDto dto) {
        String hash = this.passwordEncoder.encode(dto.getPassword());
        Optional<Roles> role = this.roleRepository.findByName("ROLE_USER");

        UserEntities user = new UserEntities(
            dto.getUsername(),
            hash,
            dto.getEmail(),
            role.get());
        
        UserEntities savedUser = this.userRepository.save(user);
        return this.userMapper.toDto(savedUser);
    }

    @Override
    public Optional<UserEntities> findByEmail(String email) {
        Optional<UserEntities> user = this.userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(user.get());
        }
    }

    @Override
    public Optional<UserEntities> findByUsername(String username) {
        Optional<UserEntities> user = this.userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(user.get());
        }
    }
}
