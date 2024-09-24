package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Collections;

@Controller
@RequestMapping("/")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
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

        // check if passwords match
        if (!password.equals(passwordConfirmation)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/register";
        }

        // call the password validation method here
        if (!isValidPassword(password)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 8 characters long, contain an uppercase letter, a special character, and a number.");
            return "redirect:/register";
        }

        try {
            // proceed with registration if validation passes
            userService.registerUser(username, password, email, Collections.singletonList(role), firstName, lastName);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully Registered");
            return "redirect:/login";
        } catch (Exception e) {
            // handle registration errors
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected error occurred.");
            return "redirect:/register";
        }
    }


    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,}$";
        return password.matches(regex);
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
                return "redirect:/admin/users";  // Admin dashboard'a yönlendir
            } else if (role.equals("ROLE_TEACHER")) {
                return "redirect:/teacher/decks";  // Öğretmen deck management sayfasına yönlendir
            } else if (role.equals("ROLE_STUDENT")) {
                return "redirect:/student/student-dashboard";  // Öğrenci çalışma sayfasına yönlendir
            }
        }

        // Varsayılan olarak ana sayfaya yönlendir
        return "redirect:/";
    }


}
