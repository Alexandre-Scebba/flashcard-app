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
@RequestMapping("/")
public class TeacherController {

    private final DeckService deckService;

    public TeacherController(DeckService deckService) {
        this.deckService = deckService;
    }

    // Show teacher dashboard
    @GetMapping("/teacher-dashboard")
    public String showTeacherDashboard() {
        return "teacher-dashboard";  // Points to teacher/teacher-dashboard.html
    }


    // Show create deck form
    @GetMapping("/deck/create")
    public String showCreateDeckForm(Model model) {
        model.addAttribute("deck", new Deck());
        return "deck-create";  // Points to deck-create.html
    }

    // Create a new deck and redirect to flashcard creation
    @PostMapping("/deck/create")
    public String createDeck(@ModelAttribute Deck deck) {
        // Get authenticated user ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();  // Fetch logged-in user

        // Create deck and save the user ID
        Deck createdDeck = deckService.createDeck(deck.getName(), userId);

        // After creating deck, redirect to flashcard creation page
        return "redirect:/teacher/flashcards/create?deckId=" + createdDeck.getId();
    }

    // View or edit a specific deck
    @GetMapping("/deck/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        model.addAttribute("deck", deck.orElse(new Deck()));
        return "deck-edit";  // Points to deck-edit.html
    }

    // Delete a deck
    @PostMapping("/deck/delete/{id}")
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

    // Add more flashcard-related functionalities here as needed...
}
