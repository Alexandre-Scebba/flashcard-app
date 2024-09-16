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


    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            // Parola sıfırlama token'ını veritabanında saklayabiliriz, örneğin expiration süresiyle

            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            // Kullanıcıya e-posta gönderme

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Password Reset Request");
            message.setText("Click the link below to reset your password: " + resetLink);

            mailSender.send(message);
        }
    }

    public boolean validateResetToken(String token) {
        // Token'ı doğrulama işlemi (Veritabanında kontrol)
        return true; // geçici doğrulama
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void savePasswordResetToken(String email, String token) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPasswordResetToken(token);
            userRepository.save(user);  // Güncellenmiş kullanıcıyı kaydet
        } else {
            // Kullanıcı bulunamazsa ne yapılacağına dair işlem ekleyebilirsiniz (loglama vs.)
            System.out.println("User not found: " + email);
        }
    }
}
