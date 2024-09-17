package com.example.flashcardtool.service;
import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;


import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String username, String password, String email, List<String> roles, String firstName, String lastName) {
        // Check if the user already exists
        if (userRepository.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("User with username " + username + " already exists.");
        }
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

    public void sendPasswordResetLink(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            // Save the token with the user or in a separate password reset token table
            // For simplicity, let's assume you have a method to save the token
            savePasswordResetToken(user, token);

            String resetLink = "http://localhost:8080/newpassword?token=" + token;
            // Send the email with the reset link
            sendEmail(user.getEmail(), "Password Reset Request", "To reset your password, click the link below:\n" + resetLink);
        } else {
            // Handle the case where the email is not found
            System.out.println("Email not found: " + email);
        }
    }

    private void savePasswordResetToken(User user, String token) {
        // Implement this method to save the token
    }

    private void sendEmail(String to, String subject, String body) {
        // Implement this method to send the email
    }

    public void updatePassword(String token, String password) {
        // Validate the token
        if (validateResetToken(token)) {
            // Find the user by the token
            Optional<User> userOptional = userRepository.findByPasswordResetToken(token);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Update the user's password
                user.setPassword(passwordEncoder.encode(password));
                // Clear the password reset token
                user.setPasswordResetToken(null);
                // Save the updated user
                userRepository.save(user);
            } else {
                // Handle the case where the user is not found
                System.out.println("User not found for token: " + token);
            }
        } else {
            // Handle the case where the token is not valid
            System.out.println("Invalid token: " + token);
        }
    }

    private boolean validateResetToken(String token) {
        // Implement this method to validate the token
        return true; // Temporary validation
    }

    public User findByUsername(String username) {
        return userRepository.getUserByUsername(username);

    }
}
