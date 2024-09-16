package com.example.flashcardtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/student-dashboard")
    public String showStudentDashboard() {
        return "student-dashboard";  // student-dashboard.html file in templates/student
    }

    @GetMapping("/logout")
    public String logout() {
        // Add your logout logic here
        return "redirect:/login";  // Redirect to login page after logout
    }
}

