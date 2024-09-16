package com.example.flashcardtool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    public SecurityConfig(UserDetailsService userDetailsService) {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login","/forgetpassword", "/newpassword","/teacher-dashboard", "/css/**", "/js/**", "/images/**").permitAll()  // Allow static resources
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")  // Define custom login page
                        .permitAll()
                        .defaultSuccessUrl("/determineRedirect", true)  // Yönlendirme işini UserController'a bırak
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")  // Redirect to login page after logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Login sonrası kullanıcı rolüne göre yönlendirme yapılacak
            response.sendRedirect("/login"); // Burası UserController tarafından yönlendirilecek
        };
    }
}
