package com.authservice.authservice.validators;

import com.authservice.authservice.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameUniqueConstraintValidator implements ConstraintValidator<UsernameUniqueConstraint, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext arg1) {
        return !this.userRepository.existsByUsername(username);
    }

}
