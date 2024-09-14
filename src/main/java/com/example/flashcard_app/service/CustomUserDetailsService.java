package com.example.flashcard_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Inject your UserRepository here to load the user from the database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement user loading logic from the database
        throw new UsernameNotFoundException("User not found");
    }
}
