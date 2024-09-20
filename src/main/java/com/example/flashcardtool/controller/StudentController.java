package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import com.example.flashcardtool.service.ProgressService;
import com.example.flashcardtool.service.StudentLibraryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/student-dashboard")
    public String showStudentDashboard(Model model) {
        String studentName = getAuthenticatedStudentName(); // Get the logged-in student's name
        model.addAttribute("studentName", studentName);
        return "student-dashboard";  // Return the correct Thymeleaf view name
    }

    // Helper method to get student name (implement as needed)
    private String getAuthenticatedStudentName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // View available decks to study
    @GetMapping("/decks")
    public String viewDecks(Model model) {
        model.addAttribute("decks", deckService.getAllDecks());
        return "student-deck-list";  // Points to student-deck-list.html
    }
<<<<<<< Updated upstream

    // Search for decks
    @GetMapping("/search")
    public String searchDecks(@RequestParam("query") String query, Model model) {
        List<Deck> searchResults = deckService.searchDecksByName(query);
        model.addAttribute("availableDecks", searchResults);  // Pass available decks for search
        return "student-library";  // Use the library view to show results and library
    }

    // View the student's library
=======
>>>>>>> Stashed changes
    @GetMapping("/library")
    public String viewLibrary(Model model) {
        String studentId = getAuthenticatedStudentId();

        // Öğrencinin kütüphanesindeki deck'leri alıyoruz
        List<StudentLibrary> studentLibrary = studentLibraryService.getLibraryByStudent(studentId);

        // Deck'leri alıp debug mesajı ekliyoruz
        List<Deck> libraryDecks = studentLibrary.stream()
                .map(library -> {
                    Deck deck = deckService.getDeckById(library.getDeckId()).orElse(new Deck());
                    System.out.println("Deck found: " + deck.getName() + ", Description: " + deck.getDescription());
                    return deck;
                })
                .collect(Collectors.toList());

        // Deck'leri modele ekliyoruz
        model.addAttribute("libraryDecks", libraryDecks);

        return "student-library";  // Ensure this view exists
    }
<<<<<<< Updated upstream
    // Add deck to student's library
=======


>>>>>>> Stashed changes
    @PostMapping("/library/add")
    public String addLibrary(@RequestParam("deckId") String deckId) {
        String studentId = getAuthenticatedStudentId(); // Get the logged-in student ID
        studentLibraryService.addLibrary(studentId, deckId);
        return "redirect:/student/library"; // Redirect to the library page
    }
<<<<<<< Updated upstream

    // Remove deck from student's library
=======
>>>>>>> Stashed changes
    @PostMapping("/library/remove")
    public String removeLibrary(@RequestParam("deckId") String deckId) {
        String studentId = getAuthenticatedStudentId(); // Get the logged-in student ID
        studentLibraryService.removeLibrary(studentId, deckId);  // Adjusted to accept both IDs
        return "redirect:/student/library"; // Redirect back to the library page
    }

    // Start studying a deck
    @GetMapping("/study/{id}")
    public String studyDeck(@PathVariable("id") String deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deckId); // flashcardService kullanarak flashcard'ları al

        // Başlangıçta ilk kart ve kartın ön yüzü gösterilecek
        model.addAttribute("deck", deck);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("currentCard", 0); // Başlangıçta ilk kart gösterilir
        model.addAttribute("showFront", true); // Başlangıçta kartın ön yüzü gösterilir
        return "study-mode";  // study-mode.html sayfasına yönlendirme yapılır
    }

    // View progress on studying
    @GetMapping("/progress")
    public String viewProgress(Model model) {
        String studentId = getAuthenticatedStudentId();  // Get the student's ID
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

    // Helper method to get authenticated student's ID
    private String getAuthenticatedStudentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Assumes the student's ID is their username
    }
}
