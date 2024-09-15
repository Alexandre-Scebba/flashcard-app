package com.example.flashcardtool.controller;

import com.example.flashcardtool.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping("/create")
    public String createFlashcard(@RequestParam String frontContent, @RequestParam String backContent, @RequestParam String deckId) {
        flashcardService.createFlashcard(frontContent, backContent, deckId);
        return "redirect:/flashcards";
    }

    @PostMapping("/update")
    public String updateFlashcard(@RequestParam String id, @RequestParam String frontContent, @RequestParam String backContent) {
        flashcardService.updateFlashcard(id, frontContent, backContent);
        return "redirect:/flashcards";
    }

    @PostMapping("/delete")
    public String deleteFlashcard(@RequestParam String id) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/flashcards";
    }
}
