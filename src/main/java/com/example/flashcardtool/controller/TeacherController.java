package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.AssignmentRequest;
import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final DeckService deckService;
    private final FlashcardService flashcardService;

    public TeacherController(DeckService deckService, FlashcardService flashcardService) {
        this.deckService = deckService;
        this.flashcardService = flashcardService;
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
        // Save the deck
        deckService.save(deck); // Save the deck

        // Redirect to the flashcard creation form with deckId
        return "redirect:/teacher/flashcards/create?deckId=" + deck.getId();
    }

    // Delete a deck
    @PostMapping("/decks/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/teacher/dashboard";  // Redirect back to the dashboard after deletion
    }

    // Helper method to get the authenticated teacher's name
    private String getAuthenticatedTeacherName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();  // Returns the authenticated teacher's username or ID
    }

    // View all decks
    @GetMapping("/decks")
    public String viewAllDecks(Model model) {
        String teacherName = getAuthenticatedTeacherName();  // Retrieve the teacher's name from the session or security context
        model.addAttribute("teacherName", teacherName);  // Pass the teacher's name to the template
        model.addAttribute("decks", deckService.getAllDecks());  // Get all decks
        return "deck-list";  // Points to deck-list.html
    }
    // Create a new deck
    @PostMapping("/create")
    public String createDeck(@ModelAttribute Deck deck) {
        String userId = getAuthenticatedTeacherName();  // Get the authenticated teacher's ID

        // Create the deck with teacher's ID
        Deck createdDeck = deckService.createDeck(deck.getName(), userId, deck.getDescription());

        // Redirect to the flashcard creation page for this deck
        return "redirect:/teacher/flashcards/create?deckId=" + createdDeck.getId();
    }

    // Add a new deck
    @PostMapping("/add")
    public String addDeck(@RequestParam String deckName, @RequestParam String deckDescription) {
        String userId = getAuthenticatedTeacherName();  // Get the authenticated teacher's ID

        // Create the deck
        deckService.createDeck(deckName, userId, deckDescription);

        return "redirect:/decks";  // Redirect to the deck list
    }
    @PostMapping("/decks/assign")
    public ResponseEntity<?> assignDeckToStudents(@RequestBody AssignmentRequest request) {
        // Retrieve the deck and assign it to the students
        deckService.assignDeckToStudents(request.getDeckId(), request.getStudentIds());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }
}
