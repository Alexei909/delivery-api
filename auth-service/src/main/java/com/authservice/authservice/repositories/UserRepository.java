package com.authservice.authservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authservice.authservice.models.UserEntities;

public interface UserRepository extends JpaRepository<UserEntities, Long> {

    Optional<UserEntities> findByUsername(String username);
    Optional<UserEntities> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
