package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.repository.DeckRepository;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import com.example.flashcardtool.service.UserService;
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
    private final FlashcardService flashcardService;
    private final UserService userService;
    private final DeckRepository deckRepository;

    public TeacherController(DeckService deckService, FlashcardService flashcardService, UserService userService, DeckRepository deckRepository) {
        this.deckService = deckService;
        this.flashcardService = flashcardService;
        this.userService = userService;
        this.deckRepository = deckRepository;
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
    public String createDeck(@ModelAttribute Deck deck, Model model) {
        deckService.save(deck); // Save the deck
        return "redirect:/teacher/flashcards/create?deckId=" + deck.getId();
    }

    // Delete a deck
    @PostMapping("/decks/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/teacher/dashboard";  // Redirect back to the dashboard after deletion
    }

    // Assign a deck to a student
    @PostMapping("/deck-assign")
    public String assignDeck(@RequestParam String deckId, @RequestParam String studentId) {
        deckService.assignDeck(deckId, studentId);
        return "redirect:/teacher/deck-assign";
    }

    @GetMapping("/deck-assign")
    public String showAssignDeckForm(Model model) {
        model.addAttribute("decks", deckService.getAllDecks());
        model.addAttribute("students", userService.findAllStudents());
        return "deck-assign";
    }

    // Helper method to get the authenticated teacher's name
    private String getAuthenticatedTeacherName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();  // Returns the authenticated teacher's username or ID
    }

    // View all decks
    @GetMapping("/decks")
    public String viewAllDecks(Model model) {
        String teacherName = getAuthenticatedTeacherName();
        model.addAttribute("teacherName", teacherName);
        model.addAttribute("decks", deckService.getAllDecks());
        return "deck-list";  // Points to deck-list.html
    }

    // Create a new deck
    @PostMapping("/create")
    public String createDeck(@ModelAttribute Deck deck) {
        String userId = getAuthenticatedTeacherName();
        Deck createdDeck = deckService.createDeck(deck.getName(), userId, deck.getDescription());
        return "redirect:/teacher/flashcards/create?deckId=" + createdDeck.getId();
    }

    // Add a new deck
    @PostMapping("/add")
    public String addDeck(@RequestParam String deckName, @RequestParam String deckDescription) {
        String userId = getAuthenticatedTeacherName();
        deckService.createDeck(deckName, userId, deckDescription);
        return "redirect:/decks";  // Redirect to the deck list
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }
}