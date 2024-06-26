package com.authservice.authservice.dtos;

import com.authservice.authservice.validators.EmailUniqueConstraint;
import com.authservice.authservice.validators.UsernameUniqueConstraint;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDto {

    @NotBlank(message = "{auth.register.dto.errors.username.empty}")
    @Size(min = 5, max = 150, message = "{auth.register.dto.errors.username.size}")
    @UsernameUniqueConstraint
    private String username;

    @NotBlank(message = "{auth.register.dto.errors.password.empty}")
    @Size(min = 6, message = "{auth.register.dto.errors.password.size}")
    private String password;

    @NotBlank(message = "{auth.register.dto.errors.email.empty}")
    @Email(message = "{auth.register.dto.errors.email.invalid}")
    @EmailUniqueConstraint
    private String email;

}
