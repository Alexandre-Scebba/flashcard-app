package com.example.flashcardtool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public SecurityConfig(UserDetailsService userDetailsService) {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
<<<<<<< Updated upstream
                        .requestMatchers("/register", "/login","/forgetpassword", "/newpassword","/css/**", "/js/**", "/images/**").permitAll()  // Allow static resources
                        .anyRequest().authenticated()
=======
                        .requestMatchers("/register", "/login", "/forgetpassword", "/newpassword", "/css/**", "/js/**", "/images/**").permitAll()  // Allow static resources and public pages
                        .requestMatchers("/teacher-dashboard").hasRole("TEACHER")  // Restrict teacher-dashboard to TEACHER role
                        .anyRequest().authenticated()  // Everything else requires authentication
>>>>>>> Stashed changes
                )
                .formLogin(login -> login
                        .loginPage("/login")  // Define custom login page
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            System.out.println("Login page is being used");
                            response.sendRedirect("/");
                        })
                )

                .logout(LogoutConfigurer::permitAll);

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
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
