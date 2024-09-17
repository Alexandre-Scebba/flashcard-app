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

    // Method to register a new user
    public void registerUser(String username, String password, String email, List<String> roles, String firstName, String lastName) {
        User user = new User();
        String userId = UUID.randomUUID().toString();  // Generate UUID
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        System.out.println("New user created with ID: " + userId);
        userRepository.save(user);
    }

    // Method to send password reset link
    public void sendPasswordResetLink(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            savePasswordResetToken(user, token);

            String resetLink = "http://localhost:8080/newpassword?token=" + token;
            sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + resetLink);
        } else {
            System.out.println("Email not found: " + email);
        }
    }

    private void savePasswordResetToken(User user, String token) {
        // Implement logic to save the token
    }

    private void sendEmail(String to, String subject, String body) {
        // Implement logic to send email
    }

    // Method to update password after validating reset token
    public void updatePassword(String token, String password) {
        if (validateResetToken(token)) {
            Optional<User> userOptional = userRepository.findByPasswordResetToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(password));
                user.setPasswordResetToken(null);  // Clear the reset token
                userRepository.save(user);
            } else {
                System.out.println("User not found for token: " + token);
            }
        } else {
            System.out.println("Invalid token: " + token);
        }
    }

    private boolean validateResetToken(String token) {
        return true;  // Temporary token validation logic
    }

    // --------------------- New Methods for Admin Functionality ---------------------

    // Method to get all users (Admin functionality)
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Fetch all users from the database
    }

    // Method to delete a user by ID (Admin functionality)
    public void deleteUser(String id) {
        userRepository.deleteById(id);  // Delete user by ID
    }
}
