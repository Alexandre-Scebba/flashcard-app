package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    // Show create deck form
    @GetMapping("/create")
    public String showCreateDeckForm(Model model) {
        model.addAttribute("deck", new Deck());  // Bind empty deck object to form
        return "deck-create";  // Points to deck-create.html
    }



    // Delete a deck by ID
    @PostMapping("/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);  // Delete the deck by ID
        return "redirect:/decks";  // Redirect to the deck list
    }

    // Update a deck by ID
    @PostMapping("/update")
    public String updateDeck(@RequestParam String id, @RequestParam String name) {
        deckService.updateDeck(id, name);  // Update the deck name
        return "redirect:/decks";  // Redirect to the deck list
    }

    // Edit deck form
    @GetMapping("/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        if (deck.isPresent()) {
            model.addAttribute("deck", deck.get());  // Add the existing deck to the model
            return "editDeck";  // Points to editDeck.html
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }

    // View a specific deck
    @GetMapping("/view/{id}")
    public String viewDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        if (deck.isPresent()) {
            model.addAttribute("deck", deck.get());  // Add the existing deck to the model
            return "viewDeck";  // Points to viewDeck.html
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }

    // Study mode: view the flashcards of a specific deck
    @GetMapping("/study/{deckId}")
    public String startStudyMode(@PathVariable("deckId") String deckId, Model model) {
        Optional<Deck> optionalDeck = deckService.getDeckById(deckId);

        // Check if the deck is present
        if (optionalDeck.isPresent()) {
            Deck deck = optionalDeck.get();  // Retrieve the deck
            List<Flashcard> flashcards = deckService.getFlashcardsForDeck(deck.getId());  // Get flashcards

            // Add deck and flashcards to the model
            model.addAttribute("deck", deck);
            model.addAttribute("flashcards", flashcards);

            return "student/study-mode";  // Points to student/study-mode.html
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }
}
