package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.model.User;
import com.example.flashcardtool.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentLibraryService studentLibraryService;
    private final DeckService deckService;
    private final ProgressService progressService;
    private final FlashcardService flashcardService;

    public StudentController(StudentLibraryService studentLibraryService, DeckService deckService,
                             ProgressService progressService, FlashcardService flashcardService) {
        this.studentLibraryService = studentLibraryService;
        this.deckService = deckService;
        this.progressService = progressService;
        this.flashcardService = flashcardService;
    }

    // Helper method to set studentName for all pages
    private void setStudentName(Model model) {
        String studentName = getAuthenticatedStudentName();
        model.addAttribute("studentName", studentName);
    }

    // Helper method to get the authenticated student's name
    private String getAuthenticatedStudentName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Helper method to get the authenticated student's ID
    private String getAuthenticatedStudentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Assuming the student's ID is their username
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Oturum açmış kullanıcının kullanıcı adını döndürür
    }


    @GetMapping("/student-dashboard")
    public String showStudentDashboard(Model model) {
        setStudentName(model); // Add studentName to the model
        return "student-dashboard";  // Return the correct Thymeleaf view
    }

    @GetMapping("/decks")
    public String viewDecks(Model model) {
        setStudentName(model); // Add studentName to the model
        model.addAttribute("decks", deckService.getAllDecks());
        return "student-deck-list";  // Points to student-deck-list.html
    }

    @GetMapping("/search")
    public String searchDecks(@RequestParam("query") String query, Model model) {
        setStudentName(model); // Add studentName to the model
        List<Deck> searchResults = deckService.searchDecksByName(query);
        model.addAttribute("availableDecks", searchResults);  // Pass available decks for search
        return "student-library";  // Use the library view to show results and library
    }

    @Autowired
    private UserService userService;

    @GetMapping("/library")
    public String viewLibrary(Model model) {
        setStudentName(model); // Add studentName to the model

        // Oturum açmış kullanıcının kullanıcı adını almak için
        String username = getAuthenticatedUsername();

        // UserService örneğini kullanarak, username ile user'ı buluyoruz

        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            String studentId = user.get().getId();  // Kullanıcının studentId'sini alıyoruz

            // AWS sorgusu studentId ile yapılacak
            List<StudentLibrary> studentLibrary = studentLibraryService.getLibraryByStudent(studentId);
            List<Deck> libraryDecks = studentLibrary.stream()
                    .map(library -> deckService.getDeckById(library.getDeckId()).orElse(new Deck()))
                    .collect(Collectors.toList());
            model.addAttribute("libraryDecks", libraryDecks);
        } else {
            System.out.println("User not found for username: " + username);
            model.addAttribute("libraryDecks", Collections.emptyList()); // Kullanıcı bulunmazsa boş liste döndürülür
        }

        return "student-library";  // Ensure this view exists
    }


    @PostMapping("/library/add")
    public String addLibrary(@RequestParam("deckId") String deckId) {
        String studentId = getAuthenticatedStudentId(); // Get the logged-in student ID
        studentLibraryService.addLibrary(studentId, deckId);
        return "redirect:/student/library"; // Redirect to the library page
    }

    @PostMapping("/library/remove")
    public String removeLibrary(@RequestParam("deckId") String deckId) {
        String studentId = getAuthenticatedStudentId(); // Get the logged-in student ID
        studentLibraryService.removeLibrary(studentId, deckId);  // Adjusted to accept both IDs
        return "redirect:/student/library"; // Redirect back to the library page
    }

    @GetMapping("/study/{id}")
    public String studyDeck(@PathVariable("id") String deckId, Model model) {
        setStudentName(model); // Add studentName to the model
        Deck deck = deckService.getDeckById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deckId);

        model.addAttribute("deck", deck);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("currentCard", 0); // Start with the first card
        model.addAttribute("showFront", true); // Show the front of the card first
        return "study-mode";  // Redirect to study-mode.html
    }

    @GetMapping("/progress")
    public String viewProgress(Model model) {
        setStudentName(model); // Add studentName to the model
        String studentId = getAuthenticatedStudentId();
        model.addAttribute("progress", progressService.getStudentProgress(studentId));
        return "progress";  // Points to student-progress.html
    }

    @GetMapping("/quiz/{id}")
    public String takeQuiz(@PathVariable String id, Model model) {
        setStudentName(model); // Add studentName to the model
        Deck deck = deckService.getDeckById(id).orElse(new Deck());
        model.addAttribute("deck", deck);
        return "quiz";  // Points to quiz.html
    }
}
