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
@RequestMapping("/")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final DeckService deckService;

<<<<<<< Updated upstream
    // Show flashcard list for management (Admin/Teacher)
    @GetMapping("/list")
=======
    public FlashcardController(FlashcardService flashcardService, DeckService deckService) {
        this.flashcardService = flashcardService;
        this.deckService = deckService;
    }

    // Tüm flashcard'ları listeler (Admin/Teacher)
    @GetMapping("/flashcard-list")
>>>>>>> Stashed changes
    public String listFlashcards(Model model) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards();
        model.addAttribute("flashcards", flashcards);
        return "flashcard-list";
    }

<<<<<<< Updated upstream
    // Show form for creating a new flashcard
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("flashcard", new Flashcard());
        return "flashcard-create";  // Path updated based on your structure
=======
    // Flashcard oluşturma formu
    @GetMapping("/flashcard-create")
    public String showCreateFlashcardForm(@RequestParam Optional<String> deckId, Model model) {
        List<Deck> decks = deckService.getAllDecks();
        model.addAttribute("decks", decks);

        // Eğer deckId verilmişse bunu flashcard'a ekle
        Flashcard flashcard = new Flashcard();
        deckId.ifPresent(flashcard::setDeckId);
        model.addAttribute("flashcard", flashcard);

        return "flashcard-create";
>>>>>>> Stashed changes
    }

    // Yeni flashcard oluşturma işlemi
    @PostMapping("/flashcard-create")
    public String createFlashcard(@ModelAttribute Flashcard flashcard) {
        flashcardService.createFlashcard(flashcard.getFrontContent(), flashcard.getBackContent(), flashcard.getDeckId());
<<<<<<< Updated upstream
        return "redirect:/flashcards/list";
=======
        return "redirect:/flashcards/flashcard-list";
>>>>>>> Stashed changes
    }

    // Var olan flashcard'ı güncelleme formu
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcardById(id);
        model.addAttribute("flashcard", flashcard);

        List<Deck> decks = deckService.getAllDecks();
        model.addAttribute("decks", decks);  // Deck listesini modele ekleyin
        return "flashcard-create";
    }

    // Flashcard güncelleme işlemi
    @PostMapping("/update/{id}")
    public String updateFlashcard(@PathVariable String id, @ModelAttribute Flashcard flashcard) {
        flashcardService.updateFlashcard(id, flashcard.getFrontContent(), flashcard.getBackContent());
<<<<<<< Updated upstream
        return "redirect:/flashcards/list";
=======
        return "redirect:/flashcards/flashcard-list";
>>>>>>> Stashed changes
    }

    // Flashcard silme işlemi
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id) {
        flashcardService.deleteFlashcard(id);
<<<<<<< Updated upstream
        return "redirect:/flashcards/list";
=======
        return "redirect:/flashcards/flashcard-list";
>>>>>>> Stashed changes
    }
}
