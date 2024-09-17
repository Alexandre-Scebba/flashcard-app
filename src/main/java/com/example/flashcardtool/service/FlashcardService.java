package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    // Create a new flashcard
    public Flashcard createFlashcard(String frontContent, String backContent, String deckId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(UUID.randomUUID().toString());
        flashcard.setFrontContent(frontContent);
        flashcard.setBackContent(backContent);
        flashcard.setDeckId(deckId);
        flashcardRepository.save(flashcard);
        return flashcard;
    }


    // Update an existing flashcard
    public void updateFlashcard(String id, String frontContent, String backContent) {
        Optional<Flashcard> optionalFlashcard = flashcardRepository.findById(id);
        if (optionalFlashcard.isPresent()) {
            Flashcard flashcard = optionalFlashcard.get();
            flashcard.setFrontContent(frontContent);
            flashcard.setBackContent(backContent);
            flashcardRepository.save(flashcard);
        }
    }

    // Delete a flashcard by ID
    public void deleteFlashcard(String id) {
        flashcardRepository.deleteById(id);
    }

    // Get a flashcard by ID
    public Flashcard getFlashcardById(String id) {
        return flashcardRepository.findById(id).orElse(null);
    }

    // Get all flashcards
    public List<Flashcard> getAllFlashcards() {
        List<Flashcard> flashcardList = new ArrayList<>();
        flashcardRepository.findAll().forEach(flashcardList::add);
        return flashcardList;
    }

    // Get all flashcards for a specific deck
    public List<Flashcard> getFlashcardsByDeckId(String deckId) {
        return flashcardRepository.findByDeckId(deckId);  // Query flashcards by deckId
    }
}
