package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    // Show flashcard list for management (Admin/Teacher)
    @GetMapping("/flashcard-list")
    public String listFlashcards(Model model) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards();
        model.addAttribute("flashcards", flashcards);
        return "flashcard-list";  // Use the correct path based on your structure
    }

    // Show form for creating a new flashcard
    @GetMapping("/flashcard-create")
    public String showCreateForm(Model model) {
        model.addAttribute("flashcard", new Flashcard());
        return "flashcard-create";  // Path updated based on your structure
    }

    // Handle flashcard creation
    @PostMapping("/create")
    public String createFlashcard(@ModelAttribute Flashcard flashcard) {
        flashcardService.createFlashcard(flashcard.getFrontContent(), flashcard.getBackContent(), flashcard.getDeckId());
        return "redirect:/flashcards/flashcard-list";  // Correct the redirect path
    }

    // Show form for updating an existing flashcard
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Flashcard flashcard = flashcardService.getFlashcardById(id);
        model.addAttribute("flashcard", flashcard);
        return "flashcard-create";  // Reusing the same form template for both create and update
    }

    // Handle flashcard update
    @PostMapping("/update/{id}")
    public String updateFlashcard(@PathVariable String id, @ModelAttribute Flashcard flashcard) {
        flashcardService.updateFlashcard(id, flashcard.getFrontContent(), flashcard.getBackContent());
        return "redirect:/flashcards/flashcard-list";  // Correct the redirect path
    }

    // Handle flashcard deletion
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/flashcards/flashcard-list";  // Correct the redirect path
    }
}
