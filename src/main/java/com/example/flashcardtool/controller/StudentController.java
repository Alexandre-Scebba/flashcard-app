package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.ProgressService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final DeckService deckService;
    private final ProgressService progressService;

    public StudentController(DeckService deckService, ProgressService progressService) {
        this.deckService = deckService;
        this.progressService = progressService;
    }

    // Student Dashboard
    @GetMapping("/student-dashboard")
    public String showStudentDashboard() {
        return "student-dashboard";  // Points to student/student-dashboard.html
    }


    // View available decks to study
    @GetMapping("/decks")
    public String viewDecks(Model model) {
        model.addAttribute("decks", deckService.getAllDecks());
        return "student-deck-list";  // Points to student-deck-list.html
    }

    // Start studying a deck
    @GetMapping("/study/{id}")
    public String studyDeck(@PathVariable String id, Model model) {
        Deck deck = deckService.getDeckById(id).orElse(new Deck());
        model.addAttribute("deck", deck);
        return "study";  // Points to study.html
    }

    // View progress on studying
    @GetMapping("/progress")
    public String viewProgress(Model model) {
        // Get the currently authenticated user (student)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentId = authentication.getName();  // You might store this differently, adjust accordingly.

        // Fetch student progress using studentId
        model.addAttribute("progress", progressService.getStudentProgress(studentId));
        return "progress";  // Points to student-progress.html
    }

    // Take a quiz on a specific deck
    @GetMapping("/quiz/{id}")
    public String takeQuiz(@PathVariable String id, Model model) {
        Deck deck = deckService.getDeckById(id).orElse(new Deck());
        model.addAttribute("deck", deck);
        return "quiz";  // Points to quiz.html
    }
}
