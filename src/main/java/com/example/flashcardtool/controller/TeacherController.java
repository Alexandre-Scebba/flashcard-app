package com.example.flashcardtool.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class TeacherController {
    @GetMapping("/teacher-dashboard")
    public String showTeacherDashboard() {
        return "teacher-dashboard";  // teacher-dashboard.html file in templates/teacher
    }

}
