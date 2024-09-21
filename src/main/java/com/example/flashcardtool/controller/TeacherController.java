package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import com.example.flashcardtool.service.StudentLibraryService;
import com.example.flashcardtool.service.UserService;
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
    private final UserService userService;
    private final StudentLibraryService studentLibraryService;

    public TeacherController(DeckService deckService, FlashcardService flashcardService, UserService userService, StudentLibraryService studentLibraryService) {
        this.deckService = deckService;
        this.flashcardService = flashcardService;
        this.userService = userService;
        this.studentLibraryService = studentLibraryService;
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
        String userId = getAuthenticatedTeacherName();
        deckService.save(deck); // Save the deck
        return "redirect:/teacher/flashcards/create?deckId=" + deck.getId();
    }

    // Delete a deck
    @PostMapping("/decks/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/teacher/decks";  // stay same page
    }

    @PostMapping("/deck-assign")
    public String assignDeck(@RequestParam String deckId, @RequestParam String studentId) {
        studentLibraryService.addLibrary(studentId, deckId);  // Assign deck to student
        return "redirect:/teacher/deck-assign";  // Redirect back to the same page after assignment
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


    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    // Show the flashcard edit form
    @GetMapping("/flashcards/edit/{id}")
    public String editFlashcard(@PathVariable String id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcardById(id);
        model.addAttribute("flashcard", flashcard);
        return "flashcard-edit";  // Points to flashcard-edit.html
    }

    // Update flashcard and return to the current deck flashcard creation page
    @PostMapping("/flashcards/edit/{id}")
    public String updateFlashcard(@ModelAttribute Flashcard flashcard, @RequestParam("deckId") String deckId) {
        flashcardService.updateFlashcard(
            flashcard.getId(),
            flashcard.getFrontContent(),
            flashcard.getBackContent(),
            flashcard.getOption1(),
            flashcard.getOption2(),
            flashcard.getOption3(),
            flashcard.getOption4()
        );
        return "redirect:/teacher/flashcards/create?deckId=" + deckId;  // Redirect back to the flashcard creation page for the current deck
    }
}