package com.example.flashcardtool.controller;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.service.DeckService;
import com.example.flashcardtool.service.FlashcardService;
import com.example.flashcardtool.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teacher/flashcards")
public class FlashcardController {

    private final FlashcardService flashcardService;
    private final DeckService deckService;
    private final FileService fileService; // Define fileService

    public FlashcardController(FlashcardService flashcardService, DeckService deckService, FileService fileService) {
        this.flashcardService = flashcardService;
        this.deckService = deckService;
        this.fileService = fileService; // Initialize fileService
    }

    @GetMapping("/create")
    public String showCreateFlashcardForm(@RequestParam("deckId") String deckId, Model model) {
        Optional<Deck> optionalDeck = deckService.getDeckById(deckId);

        if (!optionalDeck.isPresent()) {
            throw new IllegalArgumentException("Deck not found");
        }

        Deck deck = optionalDeck.get();
        Flashcard flashcard = new Flashcard();
        flashcard.setDeckId(deck.getId());

        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deck.getId());

        model.addAttribute("flashcard", flashcard);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("deck", deck);

        return "flashcard-create";
    }

    @PostMapping("/create")
    public String createFlashcard(
            @ModelAttribute Flashcard flashcard,
            @RequestParam("deckId") String deckId,
            @RequestParam("frontImage") MultipartFile frontImageFile,
            @RequestParam("backImage") MultipartFile backImageFile,
            @RequestParam("frontVideo") MultipartFile frontVideoFile,
            @RequestParam("backVideo") MultipartFile backVideoFile) {

        // Handle Front Image
        if (!frontImageFile.isEmpty()) {
            String frontImageUrl = fileService.uploadToS3(frontImageFile); // Modify to upload to S3
            flashcard.setFrontImageUrl(frontImageUrl);
        }

        // Handle Back Image
        if (!backImageFile.isEmpty()) {
            String backImageUrl = fileService.uploadToS3(backImageFile); // Modify to upload to S3
            flashcard.setBackImageUrl(backImageUrl);
        }

        // Handle Front Video
        if (!frontVideoFile.isEmpty()) {
            String frontVideoUrl = fileService.uploadToS3(frontVideoFile); // Modify to upload to S3
            flashcard.setFrontVideoUrl(frontVideoUrl);
        }

        // Handle Back Video
        if (!backVideoFile.isEmpty()) {
            String backVideoUrl = fileService.uploadToS3(backVideoFile); // Modify to upload to S3
            flashcard.setBackVideoUrl(backVideoUrl);
        }

        // Create flashcard with the given content and media
        flashcardService.createFlashcard(
                flashcard.getFrontContent(),
                flashcard.getBackContent(),
                deckId,
                flashcard.getOption1(),
                flashcard.getOption2(),
                flashcard.getOption3(),
                flashcard.getOption4(),
                flashcard.getFrontImageUrl(),
                flashcard.getBackImageUrl(),
                flashcard.getFrontVideoUrl(),
                flashcard.getBackVideoUrl()
        );

        return "redirect:/teacher/flashcards/create?deckId=" + deckId;
    }



    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable String id, @RequestParam("deckId") String deckId) {
        flashcardService.deleteFlashcard(id);
        return "redirect:/teacher/flashcards/create?deckId=" + deckId;
    }

    @GetMapping("/deck/{deckId}")
    public String listFlashcardsByDeck(@PathVariable String deckId, Model model) {
        model.addAttribute("flashcards", flashcardService.getFlashcardsByDeckId(deckId));
        return "teacher/flashcard-list";
    }

    @GetMapping
    public String listAllFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardService.getAllFlashcards());
        return "teacher/flashcard-list";
    }

    @GetMapping("/study/{deckId}")
    public String studyDeck(@PathVariable String deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId).orElseThrow(() -> new IllegalArgumentException("Deck not found"));
        List<Flashcard> flashcards = flashcardService.getFlashcardsByDeckId(deckId);

        // Debug logs
        flashcards.forEach(card -> {
            System.out.println("Flashcard ID: " + card.getId());
            System.out.println("Front Image URL: " + card.getFrontImageUrl());
            System.out.println("Back Image URL: " + card.getBackImageUrl());
        });

        model.addAttribute("deck", deck);
        model.addAttribute("flashcards", flashcards);
        model.addAttribute("currentCard", 0);

        return "study-mode";
    }

}