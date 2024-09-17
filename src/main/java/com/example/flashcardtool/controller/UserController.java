package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password,
                               @RequestParam String email, @RequestParam String role,
                               @RequestParam String firstName, @RequestParam String lastName) {
        userService.registerUser(username, password, email, Collections.singletonList(role), firstName, lastName);
        return "redirect:/login";
    }

    @GetMapping("/determineRedirect")
    public String determineRedirect() {
        // Authentication nesnesini al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kullanıcının rolünü belirle
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin-dashboard";  // Admin dashboard'a yönlendir
            } else if (role.equals("ROLE_TEACHER")) {
                return "redirect:/teacher/dashboard";  // Öğretmen deck management sayfasına yönlendir
            } else if (role.equals("ROLE_STUDENT")) {
                return "redirect:/student-dashboard";  // Öğrenci çalışma sayfasına yönlendir
            }
        }

        // Varsayılan olarak ana sayfaya yönlendir
        return "redirect:/";
    }


}
