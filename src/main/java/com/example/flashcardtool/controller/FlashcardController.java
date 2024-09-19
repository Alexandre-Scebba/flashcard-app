package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final DeckService deckService;

    public FlashcardController(FlashcardService flashcardService, DeckService deckService) {
        this.flashcardService = flashcardService;
        this.deckService = deckService;
    }

    // Show the flashcard creation form for a specific deck
    @GetMapping("/create")
    public String showCreateFlashcardForm(@RequestParam("deckId") String deckId, Model model) {
        // Retrieve the deck by ID
        Optional<Deck> optionalDeck = deckService.getDeckById(deckId);

        if (!optionalDeck.isPresent()) {
            throw new IllegalArgumentException("Deck not found");
        }

        Deck deck = optionalDeck.get();

        // Prepare the Flashcard for creation
        Flashcard flashcard = new Flashcard();
        flashcard.setDeckId(deck.getId());  // Associate the flashcard with the deck ID

        // Get all flashcards in this deck
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deck.getId());

        // Add attributes to the model for rendering
        model.addAttribute("flashcard", flashcard);
        model.addAttribute("flashcards", flashcards);  // Display already added flashcards
        model.addAttribute("deck", deck);  // Pass the deck information for reference

        return "flashcard-create";  // Ensure this template file exists
    }

    // Process flashcard creation and return to the same page to add more flashcards
    @PostMapping("/create")
    public String createFlashcard(@ModelAttribute Flashcard flashcard, @RequestParam("deckId") String deckId) {
        flashcardService.createFlashcard(flashcard.getFrontContent(), flashcard.getBackContent(), deckId, flashcard.getOption1(), flashcard.getOption2(), flashcard.getOption3(), flashcard.getOption4());
        return "redirect:/teacher/flashcards/create?deckId=" + deckId;  // Stay on flashcard creation page for the same deck
    }

    // Delete flashcard and stay on the same flashcard creation page
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id, @RequestParam("deckId") String deckId) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/teacher/flashcards/create?deckId=" + deckId;  // Redirect to the flashcards for the current deck
    }

    // View all flashcards for a specific deck
    @GetMapping("/deck/{deckId}")
    public String listFlashcardsByDeck(@PathVariable String deckId, Model model) {
        model.addAttribute("flashcards", flashcardService.getFlashcardsByDeckId(deckId));
        return "teacher/flashcard-list";  // Points to flashcard-list.html for the specific deck
    }

    // View all flashcards (global list)
    @GetMapping
    public String listAllFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getAllFlashcards());
        return "teacher/flashcard-list";  // Points to a global flashcard list
    }
}