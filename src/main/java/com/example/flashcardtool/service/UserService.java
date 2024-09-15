package com.example.flashcardtool.service;

import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String email, List<String> roles, String firstName, String lastName) {
        User user = new User();
        String userId = UUID.randomUUID().toString();  // UUID oluşturuluyor
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        System.out.println("Yeni oluşturulan kullanıcı ID: " + userId);
        userRepository.save(user);
    }

}
