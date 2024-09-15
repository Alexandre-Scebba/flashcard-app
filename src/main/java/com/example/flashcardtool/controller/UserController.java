package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String postLogin() {
        // Add logic to determine the user's role after successful login
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN") || role.equals("ROLE_TEACHER")) {
                // Redirect Teacher or Admin to the deck management page
                return "redirect:/decks";  // Adjust the URL as per your routing
            } else if (role.equals("ROLE_STUDENT")) {
                // Redirect Student to the study page
                return "redirect:/student/study";  // Adjust the URL as per your routing
            }
        }

        // Default redirection if no role is found
        return "redirect:/";
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

}
