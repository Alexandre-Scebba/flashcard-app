package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.*;
import com.example.flashcardtool.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.flashcardtool.dto.ProgressDTO;


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

        // Fetch the latest progress data for the student
        String studentId = getAuthenticatedStudentId();
        List<Progress> progressList = progressService.getProgressByStudentId(studentId);

        if (!progressList.isEmpty()) {
            Progress latestProgress = progressList.get(progressList.size() - 1);
            model.addAttribute("studyTime", latestProgress.getStudyTime());
            model.addAttribute("correctAnswers", latestProgress.getCorrectAnswers());
            model.addAttribute("incorrectAnswers", latestProgress.getIncorrectAnswers());

            // Resolve deck names for recent activity
            List<ProgressDTO> recentProgressDTOs = progressList.stream()
                    .limit(3)
                    .map(progress -> {
                        Deck deck = deckService.getDeckById(progress.getDeckId()).orElse(new Deck());
                        return new ProgressDTO(deck.getName(), progress.getStudyTime(), progress.getCorrectAnswers(), progress.getIncorrectAnswers());
                    }).collect(Collectors.toList());

            model.addAttribute("recentProgress", recentProgressDTOs);
        } else {
            model.addAttribute("recentProgress", Collections.emptyList());
        }

        // Fetch recent decks in the library
        Optional<User> user = userService.findByUsername(getAuthenticatedUsername());
        if (user.isPresent()) {
            String studentIdFromUser = user.get().getId();
            List<StudentLibrary> studentLibrary = studentLibraryService.getLibraryByStudent(studentIdFromUser);
            List<Deck> libraryDecks = studentLibrary.stream()
                    .map(library -> deckService.getDeckById(library.getDeckId()).orElse(new Deck()))
                    .collect(Collectors.toList());

            model.addAttribute("recentDecks", libraryDecks.stream()
                    .sorted((d1, d2) -> d2.getId().compareTo(d1.getId()))
                    .limit(3)
                    .collect(Collectors.toList()));
        } else {
            model.addAttribute("recentDecks", Collections.emptyList());
        }

        return "student-dashboard";
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
        // Username yerine studentId'yi almak için doğru kullanıcı ID'sini çekiyoruz.
        String username = getAuthenticatedUsername(); // Kullanıcı adı alınıyor
        Optional<User> user = userService.findByUsername(username); // Kullanıcıyı buluyoruz

        if (user.isPresent()) {
            String studentId = user.get().getId(); // Kullanıcının gerçek student ID'sini alıyoruz
            studentLibraryService.addLibrary(studentId, deckId); // Deck'i library'ye ekliyoruz
        } else {
            System.out.println("User not found for username: " + username);
        }
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

        // Initialize the study session by starting with the first flashcard
        model.addAttribute("deck", deck);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("currentCard", 0); // Start with the first card
        model.addAttribute("correctAnswers", 0);
        model.addAttribute("incorrectAnswers", 0);
        model.addAttribute("showFront", true); // Show the front of the card first

        // Shuffle options for the first card and pass them to the model
        model.addAttribute("flashcardOptions", flashcardService.getShuffledOptions(flashcards.get(0)));

        return "study-mode";  // Redirect to study-mode.html
    }

    @PostMapping("/study/{id}")
    public String submitAnswer(
            @PathVariable("id") String deckId,
            @RequestParam("selectedOption") String selectedOption,
            @RequestParam("currentCard") int currentCard,
            @RequestParam("correctAnswers") int correctAnswers,
            @RequestParam("incorrectAnswers") int incorrectAnswers,
            Model model) {

        Deck deck = deckService.getDeckById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deckId);

        // Ensure that the currentCard index is within the flashcards list size
        if (currentCard >= flashcards.size()) {
            // End of flashcards, display results
            model.addAttribute("studyFinished", true);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("incorrectAnswers", incorrectAnswers);

            // Progress verisini kaydedelim
            Progress progress = new Progress();
            progress.setStudentId(getAuthenticatedStudentId()); // Oturum açan öğrencinin ID'si
            progress.setDeckId(deckId);
            progress.setCorrectAnswers(correctAnswers);
            progress.setIncorrectAnswers(incorrectAnswers);
            progress.setStudyTime(calculateStudyTime()); // Çalışma süresi hesaplama
            progress.setPercentage(calculatePercentage(correctAnswers, incorrectAnswers)); // Yüzdeyi hesapla

            // Progress kaydedelim
            progressService.saveProgress(progress);

            return "study-results"; // Sonuçları gösterecek bir sayfa oluştur
        }

        // Current flashcard
        Flashcard currentFlashcard = flashcards.get(currentCard);

        // Check if selected answer matches option1 (correct answer)
        if (selectedOption.equals(currentFlashcard.getOption1())) {
            correctAnswers++;
            model.addAttribute("feedback", "Correct answer!");  // Flash message for correct answer
        } else {
            incorrectAnswers++;
            model.addAttribute("feedback", "Wrong, try again.");  // Flash message for incorrect answer
        }

        // Move to the next card or finish if it's the last card
        currentCard++;

        if (currentCard < flashcards.size()) {
            model.addAttribute("currentCard", currentCard);
            model.addAttribute("flashcardOptions", flashcardService.getShuffledOptions(flashcards.get(currentCard)));
        } else {
            // Study session tamamlandığında
            model.addAttribute("studyFinished", true);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("incorrectAnswers", incorrectAnswers);

            // Progress verisini kaydedelim
            Progress progress = new Progress();
            progress.setStudentId(getAuthenticatedStudentId()); // Oturum açan öğrencinin ID'si
            progress.setDeckId(deckId);
            progress.setCorrectAnswers(correctAnswers);
            progress.setIncorrectAnswers(incorrectAnswers);
            progress.setStudyTime(calculateStudyTime()); // Çalışma süresi hesaplama
            progress.setPercentage(calculatePercentage(correctAnswers, incorrectAnswers)); // Yüzdeyi hesapla

            // Progress kaydedelim
            progressService.saveProgress(progress);

            return "study-complete"; // Sonuçları gösterecek bir sayfa oluştur
        }

        model.addAttribute("deck", deck);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("incorrectAnswers", incorrectAnswers);

        return "study-mode";
    }

    private long calculateStudyTime() {
        long startTime = System.currentTimeMillis();
        // Study mode bittiğinde çalışma süresi hesaplanmalı
        long endTime = System.currentTimeMillis();
        return endTime - startTime; // Çalışma süresi milisaniye olarak döndürülür
    }

    private double calculatePercentage(int correctAnswers, int incorrectAnswers) {
        int totalAnswers = correctAnswers + incorrectAnswers;
        if (totalAnswers == 0) {
            return 0.0;
        }
        return ((double) correctAnswers / totalAnswers) * 100;
    }

    @GetMapping("/progress")
    public String viewProgress(Model model) {
        // add recent study and quiz data from DynamoDB database for chart and review
        String studentId = getAuthenticatedStudentId();
        List<Progress> progressList = progressService.getProgressByStudentId(studentId);

        // Fetch the latest progress data for the student
        if (!progressList.isEmpty()) {
            Progress latestProgress = progressList.get(progressList.size() - 1);
            model.addAttribute("studyTime", latestProgress.getStudyTime());
            model.addAttribute("correctAnswers", latestProgress.getCorrectAnswers());
            model.addAttribute("incorrectAnswers", latestProgress.getIncorrectAnswers());

            // Resolve deck names for recent activity
            List<ProgressDTO> recentProgressDTOs = progressList.stream()
                    .limit(3)
                    .map(progress -> {
                        Deck deck = deckService.getDeckById(progress.getDeckId()).orElse(new Deck());
                        return new ProgressDTO(deck.getName(), progress.getStudyTime(), progress.getCorrectAnswers(), progress.getIncorrectAnswers());
                    }).collect(Collectors.toList());

            model.addAttribute("recentProgress", recentProgressDTOs);
        } else {
            model.addAttribute("recentProgress", Collections.emptyList());
        }

        return "progress";  // Points to progress.html
    }

    @GetMapping("/quiz/{id}")
    public String takeQuiz(@PathVariable String id, Model model) {
        setStudentName(model); // Add studentName to the model
        Deck deck = deckService.getDeckById(id).orElse(new Deck());
        model.addAttribute("deck", deck);
        return "quiz";  // Points to quiz.html
    }
}
