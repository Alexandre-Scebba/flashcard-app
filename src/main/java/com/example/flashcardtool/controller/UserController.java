package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/")
@Slf4j  // Add this annotation to enable logging
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
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String passwordConfirmation,
                               @RequestParam String email,
                               @RequestParam String role,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordConfirmation)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/register";
        }

        try {
            userService.registerUser(username, password, email, Collections.singletonList(role), firstName, lastName);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully Registered");  // Success message
            log.info("Successfully registered");
            return "redirect:/login";
        } catch (UserService.UserAlreadyExistError error) {
            log.error(error.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "User Already Exists, try a new name");  // Error message
            return "redirect:/register";
        } catch (Exception error) {
            log.error(error.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error");
            return "redirect:/register";
        }
    }

    @GetMapping("/determineRedirect")
    public String determineRedirect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin-dashboard";
            } else if (role.equals("ROLE_TEACHER")) {
                return "redirect:/teacher/dashboard";
            } else if (role.equals("ROLE_STUDENT")) {
                return "redirect:/student/student-dashboard";
            }
        }

        return "redirect:/";
    }
}