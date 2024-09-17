package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.service.DeckService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/decks")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    // Yeni deck oluşturma sayfası
    @GetMapping("/create")
    public String showCreateDeckForm(Model model) {
        model.addAttribute("deck", new Deck());
        return "deck-create";
    }

    // Yeni deck oluşturma işlemi
    @PostMapping("/create")
    public String createDeck(@ModelAttribute Deck deck) {
        // Authenticated kullanıcı ID'sini al
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();  // Giriş yapmış kullanıcıdan user ID'sini al

        // Deck oluştur ve kullanıcı ID'sini kaydet
        Deck createdDeck = deckService.createDeck(deck.getName(), userId);

        // Deck oluşturulduktan sonra flashcard ekleme sayfasına yönlendir
        return "redirect:/flashcard-create?deckId=" + createdDeck.getId();
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

    @PostMapping("/update")
    public String updateDeck(@RequestParam String id, @RequestParam String name) {
        deckService.updateDeck(id, name);
        return "redirect:/decks";
    }

    @GetMapping("/edit/{id}")
    public String editDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        model.addAttribute("deck", deck.orElse(new Deck()));
        return "editDeck";
    }

    @GetMapping("/view/{id}")
    public String viewDeck(@PathVariable String id, Model model) {
        Optional<Deck> deck = deckService.getDeckById(id);
        model.addAttribute("deck", deck.orElse(new Deck()));
        return "viewDeck";
    }
}