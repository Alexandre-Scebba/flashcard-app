package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/decks")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @GetMapping
    public String showDeckManagementPage(Model model) {
        // Load the list of decks for the teacher/admin
        List<Deck> decks = deckService.getAllDecks();
        model.addAttribute("decks", decks);
        return "deckManagement";  // Ensure this corresponds to your Thymeleaf template
    }

    @PostMapping("/add")
    public String addDeck(@RequestParam String deckName, @RequestParam String deckDescription) {
        deckService.createDeck(deckName, deckDescription);
        return "redirect:/decks";
    }

    @PostMapping("/delete/{id}")
    public String deleteDeck(@PathVariable String id) {
        deckService.deleteDeck(id);
        return "redirect:/decks";
    }

    @PostMapping("/create")
    public String createDeck(@RequestParam String name, @RequestParam String userId) {
        deckService.createDeck(name, userId);
        return "redirect:/decks";
    }

    @PostMapping("/update")
    public String updateDeck(@RequestParam String id, @RequestParam String name) {
        deckService.updateDeck(id, name);
        return "redirect:/decks";
    }

   // update edit deck
    @GetMapping("/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        model.addAttribute("deck", deck);
        return "editDeck";
    }

    // update view deck
    @GetMapping("/view/{id}")
    public String viewDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        model.addAttribute("deck", deck);
        return "viewDeck";
    }
}
