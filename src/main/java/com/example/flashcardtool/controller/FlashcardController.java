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

    @GetMapping("/create")
    public String showCreateFlashcardForm(@RequestParam("deckName") String deckName, Model model) {
        // Retrieve the deck by name
        Optional<Deck> optionalDeck = deckService.findByName(deckName);  // Make sure this method is defined in DeckService

        if (!optionalDeck.isPresent()) {
            throw new IllegalArgumentException("Deck not found");
        }

        Deck deck = optionalDeck.get(); // Unwrap the Optional<Deck> safely

        // Prepare the Flashcard for creation
        Flashcard flashcard = new Flashcard();
        flashcard.setDeckId(deck.getId()); // Associate with the deck by ID

        // Get all flashcards in this deck
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deck.getId());

        // Add attributes to the model for rendering
        model.addAttribute("flashcard", flashcard);
        model.addAttribute("flashcards", flashcards); // Display already added flashcards
        model.addAttribute("deckName", deckName); // Keep deckName for reference

        return "/flashcard-create";  // Ensure this template file exists
    }


    // Process flashcard creation and return to the same page to add more flashcards
    @PostMapping("/create")
    public String createFlashcard(@ModelAttribute Flashcard flashcard, @RequestParam("deckId") String deckId, @RequestParam("deckName") String deckName) {
        flashcardService.createFlashcard(flashcard.getFrontContent(), flashcard.getBackContent(), deckId);
        return "redirect:/teacher/flashcards/create?deckName=" + deckName; // Stay on flashcard creation page
    }

    // Edit flashcard
    @GetMapping("/edit/{id}")
    public String editFlashcard(@PathVariable String id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcardById(id);
        model.addAttribute("flashcard", flashcard);
        return "teacher/flashcard-edit";  // Points to flashcard-edit.html
    }

    // Update flashcard and return to the current deck flashcard creation page
    @PostMapping("/edit/{id}")
    public String updateFlashcard(@ModelAttribute Flashcard flashcard, @RequestParam("deckId") String deckId, @RequestParam("deckName") String deckName) {
        flashcardService.updateFlashcard(flashcard.getId(), flashcard.getFrontContent(), flashcard.getBackContent());
        return "redirect:/teacher/flashcards/create?deckName=" + deckName; // Redirect back to the flashcard creation page for the current deck
    }

    // Delete flashcard and stay on the same flashcard creation page
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id, @RequestParam("deckId") String deckId, @RequestParam("deckName") String deckName) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/teacher/flashcards/create?deckName=" + deckName;  // Redirect to the flashcards for the current deck
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