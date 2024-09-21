package com.example.flashcardtool.service;

import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public void registerUser(String username, String password, String email, List<String> roles, String firstName, String lastName) {
        User user = new User();
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
    }

    // Send password reset link
    public void sendPasswordResetLink(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            savePasswordResetToken(user, token);

            String resetLink = "http://localhost:8080/newpassword?token=" + token;
            sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + resetLink);
        } else {
            System.out.println("User with email " + email + " not found.");
        }
    }


    // Save password reset token
    private void savePasswordResetToken(User user, String token) {
        user.setPasswordResetToken(token);
        userRepository.save(user); // Save user with the reset token
    }

    // Send email logic (mock implementation)
    private void sendEmail(String to, String subject, String body) {
        // Implement actual email-sending logic here
        System.out.println("Sending email to: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }

    // Update password after validating reset token
    public void updatePassword(String token, String password) {
        if (validateResetToken(token)) {
            Optional<User> userOptional = userRepository.findByPasswordResetToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(password));
                user.setPasswordResetToken(null); // Clear the token once the password is updated
                userRepository.save(user);
                System.out.println("Password updated for user: " + user.getUsername());
            } else {
                System.out.println("Invalid token.");
            }
        } else {
            System.out.println("Token validation failed.");
        }
    }

    // Validate the password reset token
    private boolean validateResetToken(String token) {
        // Logic to validate the token (e.g., check expiration)
        return token != null && !token.isEmpty();
    }

    // Get all users (Admin functionality)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a user by ID (Admin functionality)
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // Find all students (Filtering for role-based queries)
    public List<User> findAllStudents() {
        return userRepository.findAllStudents(); // Assumes a repository method that filters students based on roles
    }
}
