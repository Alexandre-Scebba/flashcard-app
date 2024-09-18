package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final DeckService deckService;

    public FlashcardController(FlashcardService flashcardService, DeckService deckService) {
        this.flashcardService = flashcardService;
        this.deckService = deckService;
    }

    // Show create flashcard form
    @GetMapping("/create")
    public String showCreateFlashcardForm(@RequestParam("deckId") String deckId, Model model) {
        Flashcard flashcard = new Flashcard();
        flashcard.setDeckId(deckId); // Pre-fill the deck ID
        model.addAttribute("flashcard", flashcard);
        model.addAttribute("decks", deckService.findAll()); // Send all decks in case of manual selection
        return "flashcard-create"; // Points to flashcard-create.html
    }

    // Create a flashcard
    @PostMapping("/create")
    public String createFlashcard(@ModelAttribute Flashcard flashcard, @RequestParam("deckId") String deckId) {
        flashcard.setDeckId(deckId); // Associate flashcard with the deck
        flashcardService.createFlashcard(flashcard.getFrontContent(), flashcard.getBackContent(), deckId);
        return "redirect:/teacher/decks";  // Redirect to the list of decks
    }

    // Edit flashcard
    @GetMapping("/edit/{id}")
    public String editFlashcard(@PathVariable String id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcardById(id);
        model.addAttribute("flashcard", flashcard);
        return "flashcard-edit";  // Points to flashcard-edit.html
    }

    // Delete flashcard
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/teacher/flashcards";  // Redirect to the flashcard list or another appropriate page
    }

    // View all flashcards
    @GetMapping
    public String listFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getAllFlashcards());
        return "flashcard-list";  // Points to flashcard-list.html
    }
}
