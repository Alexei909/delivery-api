package com.authservice.authservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authservice.authservice.models.UserEntities;
import com.authservice.authservice.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntities user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Неверный логин или пароль"));

        return UserDetailsImpl.build(user);
    }

}
