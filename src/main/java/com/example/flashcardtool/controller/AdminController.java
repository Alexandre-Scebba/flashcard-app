package com.example.flashcardtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // Mapping for Admin Dashboard
    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "/dashboard";  // Direct to admin/dashboard.html
    }

    // Mapping for Deck Management under the admin section
    @GetMapping("/deck-management")
    public String showDeckManagement() {
        return "/deck-management";  // Direct to admin/deck-management.html
    }

}
