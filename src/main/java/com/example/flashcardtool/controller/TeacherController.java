package com.example.flashcardtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherController {

    @GetMapping("/teacher-dashboard")
    public String showTeacherDashboard() {
        return "teacher-dashboard";  // This corresponds to /templates/teacher-dashboard.html
    }
}
