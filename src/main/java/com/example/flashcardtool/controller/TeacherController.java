package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final DeckService deckService;

    public TeacherController(DeckService deckService) {
        this.deckService = deckService;
    }

    // Show teacher dashboard
    @GetMapping("/dashboard")
    public String showTeacherDashboard() {
        return "teacher-dashboard";  // Points to teacher/teacher-dashboard.html
    }

    // Show create deck form
    @GetMapping("/decks/create")
    public String showCreateDeckForm(Model model) {
        model.addAttribute("deck", new Deck());
        return "deck-create";  // Points to deck-create.html
    }

    // Create a new deck and redirect to flashcard creation
    @PostMapping("/decks/create")
    public String createDeck(@ModelAttribute Deck deck) {
        // Get authenticated user ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();  // Fetch logged-in user

        // Create deck and save the user ID
        Deck createdDeck = deckService.createDeck(deck.getName(), userId);

        // After creating deck, redirect to flashcard creation page
        return "redirect:/teacher/flashcards/create?deckId=" + createdDeck.getId();
    }

    // Delete a deck
    @PostMapping("/decks/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/teacher/dashboard";  // Redirect back to the dashboard after deletion
    }

    // View all decks for the teacher
    @GetMapping("/decks")
    public String viewAllDecks(Model model) {
        model.addAttribute("decks", deckService.getAllDecks());
        return "deck-list";  // Points to deck-list.html
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }
}