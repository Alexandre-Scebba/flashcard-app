package com.example.flashcardtool.service;

import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Send password reset email
    public void sendPasswordResetEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email); // Optional<User> is returned here

        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Unwrap the Optional
            String token = UUID.randomUUID().toString();
            // Save token in the database (in the User entity)
            savePasswordResetToken(email, token);

            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // Send email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password Reset Request");
            message.setText("Click the link below to reset your password: " + resetLink);

            mailSender.send(message);
            System.out.println("Password reset email sent to: " + email);
        } else {
            System.out.println("User not found with email: " + email);
        }
    }

    // Validate password reset token
    public boolean validateResetToken(String token) {
        Optional<User> userOptional = userRepository.findByPasswordResetToken(token);
        return userOptional.isPresent(); // Return true if a user with the token exists
    }

    // Update the password
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);  // Clear the reset token after successful password update
        userRepository.save(user);
        System.out.println("Password updated for user: " + user.getUsername());
    }

    // Save the password reset token in the database
    public void savePasswordResetToken(String email, String token) {
        Optional<User> userOptional = userRepository.findByEmail(email); // Optional<User> is returned here
        if (userOptional.isPresent()) {
            User user = userOptional.get();  // Unwrap the Optional
            user.setPasswordResetToken(token);
            userRepository.save(user);  // Save the updated user with the reset token
            System.out.println("Password reset token saved for user: " + email);
        } else {
            System.out.println("User not found: " + email);
        }
    }
}
