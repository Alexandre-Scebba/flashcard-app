package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.User;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "admin-dashboard";  // Points to admin/admin-dashboard.html
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

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable String id, Model model) {
        User user = userService.findById(id).get();
        List<String> allRoles = List.of("ROLE_ADMIN","ROLE_TEACHER","ROLE_STUDENT");
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable String id, @ModelAttribute User user) {
        user.setId(id);  // Ensure the user ID is set
        userService.updateUser(user);
        return "redirect:/admin/users";  // Redirect to user management after update
    }

    // **New Mapping: Reset Password**
    @GetMapping("/users/reset-password/{id}")
    public String resetUserPassword(@PathVariable String id) {
        userService.resetPassword(id);
        return "redirect:/admin/users";  // Redirect back to user management after reset
    }

    @GetMapping("users/create")
    public String getCreateUser() {
        return "create-user-admin"; // TODO
    }

    @PostMapping("users/create")
    public String createUser() {
        //TODO
        return "redirect:/admin/users";
    }


//    // Manage Decks
//    @GetMapping("/decks")
//    public String manageDecks(Model model) {
//        model.addAttribute("decks", deckService.getAllDecks());
//        return "deck-management";  // Points to deck-management.html
//    }
//
//    // Edit a deck
//    @GetMapping("/decks/edit/{id}")
//    public String editDeck(@PathVariable String id, Model model) {
//        model.addAttribute("deck", deckService.getDeckById(id).orElse(new Deck()));
//        return "deck-edit";  // Points to deck-edit.html
//    }
//
//    // Delete a deck
//    @PostMapping("/decks/delete/{id}")
//    public String deleteDeck(@PathVariable String id) {
//        deckService.deleteDeck(id);
//        return "redirect:/admin/decks";  // Redirect to deck management after deletion
//    }
}
