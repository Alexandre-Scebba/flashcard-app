package com.example.flashcardtool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig(UserDetailsService userDetailsService) {
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/forgetpassword", "/newpassword").permitAll() // Giriş gerektirmeyen sayfalar
                        .requestMatchers("/teacher/**", "/decks/**").hasRole("TEACHER") // Öğretmen sayfaları
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin sayfaları
                        .requestMatchers("/student/**").hasRole("STUDENT") // Öğrenci sayfaları
                        .anyRequest().authenticated() // Diğer tüm sayfalar için giriş gereksinimi
                )
                .formLogin(login -> login
                        .loginPage("/login")  // Özel login sayfası
                        .permitAll()
                        .defaultSuccessUrl("/determineRedirect", true) // Giriş sonrası yönlendirme
                        .failureUrl("/login?error=true") // auth fail redirect for flash-message logic
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());


        return http.build();
    }

}
