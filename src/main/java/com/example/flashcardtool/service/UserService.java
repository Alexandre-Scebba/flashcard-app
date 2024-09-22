package com.example.flashcardtool.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public static class UserAlreadyExistError extends RuntimeException {}
    // Register a new user
    public void registerUser(String username, String password, String email, List<String> roles, String firstName, String lastName) {

        if (existsByUsername(username)) {
            throw new UserAlreadyExistError();
        }

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

    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
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

    public Optional<User> findByUsername(String username) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":username", new AttributeValue().withS(username));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("username = :username")
                .withExpressionAttributeValues(eav);

        List<User> result = dynamoDBMapper.scan(User.class, scanExpression);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<User> findAllStudents() {
        Map<String, String> ean = new HashMap<>();
        ean.put("#roles", "roles"); // Alias for the reserved keyword 'roles', cant use roles since its ddb keyword

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":role", new AttributeValue().withS("ROLE_STUDENT"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(#roles, :role)")
                .withExpressionAttributeNames(ean)
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(User.class, scanExpression);
    }


    public Optional<User> findById(String studentId) {
        return userRepository.getUserById(studentId);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    // Reset user password (Admin functionality)
    public void resetPassword(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String defaultPassword = "Password@123";
            user.setPassword(passwordEncoder.encode(defaultPassword));
            String resetToken = UUID.randomUUID().toString();
            user.setPasswordResetToken(resetToken);
            userRepository.save(user);

            // TODO -> SendPasswordResetEmail(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }


}
