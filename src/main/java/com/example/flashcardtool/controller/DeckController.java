package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckService deckService;
    private final UserService userService;

    public DeckController(DeckService deckService, UserService userService) {
        this.deckService = deckService;
        this.userService = userService;
    }

    // Show create deck form
    @GetMapping("/create")
    public String showCreateDeckForm(Model model) {
        model.addAttribute("deck", new Deck());
        return "deck-create";
    }

    // Delete a deck by ID
    @PostMapping("/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/decks";
    }

    // Update a deck by ID
    @PostMapping("/update")
    public String updateDeck(@RequestParam String id, @RequestParam String name) {
        deckService.updateDeck(id, name);
        return "redirect:/decks";
    }

    // Edit deck form
    @GetMapping("/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        if (deck.isPresent()) {
            model.addAttribute("deck", deck.get());
            return "editDeck";
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }

    // View a specific deck
    @GetMapping("/view/{id}")
    public String viewDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        if (deck.isPresent()) {
            model.addAttribute("deck", deck.get());
            return "viewDeck";
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }

    // Study mode: view the flashcards of a specific deck
    @GetMapping("/study/{deckId}")
    public String startStudyMode(@PathVariable("deckId") String deckId, Model model) {
        Optional<Deck> optionalDeck = deckService.getDeckById(deckId);
        if (optionalDeck.isPresent()) {
            Deck deck = optionalDeck.get();
            model.addAttribute("deck", deck);
            model.addAttribute("flashcards", deckService.getFlashcardsForDeck(deck.getId()));
            return "student/study-mode";
        } else {
            throw new IllegalArgumentException("Deck not found");
        }
    }


}