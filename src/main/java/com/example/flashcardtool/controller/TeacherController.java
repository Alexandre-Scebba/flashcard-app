
package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

<<<<<<< Updated upstream
    // View all decks for the teacher
=======
    @PostMapping("/deck-assign")
    public String assignDeck(@RequestParam String deckId, @RequestParam String studentId, RedirectAttributes redirectAttributes) {
        String teacherName = getAuthenticatedTeacherName(); // Get the authenticated teacher's name
        try {
            if (studentLibraryService.isDeckAlreadyAssigned(studentId, deckId)) {
                redirectAttributes.addFlashAttribute("message", "Deck is already assigned to the student.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-warning");
            } else {
                studentLibraryService.assignDeckToStudent(studentId, deckId); // Actual assignment
                redirectAttributes.addFlashAttribute("message", "Deck successfully assigned to the student.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to assign deck.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
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
>>>>>>> Stashed changes
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
