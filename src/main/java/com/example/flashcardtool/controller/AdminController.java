package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DeckService deckService;
    private final UserService userService;

    public AdminController(DeckService deckService, UserService userService) {
        this.deckService = deckService;
        this.userService = userService;
    }

    // Admin Dashboard
    @GetMapping("/admin-dashboard")
    public String showAdminDashboard() {
        return "admin/admin-dashboard";  // Points to admin/admin-dashboard.html
    }

    // Manage Users
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-management";  // Points to user-management.html
    }

    // Delete a user
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";  // Redirect to user management after deletion
    }

    // Manage Decks
    @GetMapping("/decks")
    public String manageDecks(Model model) {
        model.addAttribute("decks", deckService.getAllDecks());
        return "deck-management";  // Points to deck-management.html
    }

    // Edit a deck
    @GetMapping("/decks/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        model.addAttribute("deck", deckService.getDeckById(id).orElse(new Deck()));
        return "deck-edit";  // Points to deck-edit.html
    }

    // Delete a deck
    @PostMapping("/decks/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/admin/decks";  // Redirect to deck management after deletion
    }
}
