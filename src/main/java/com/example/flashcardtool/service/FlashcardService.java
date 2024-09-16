package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    // Create new flashcard
    public Flashcard createFlashcard(String frontContent, String backContent, String deckId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(UUID.randomUUID().toString());
        flashcard.setFrontContent(frontContent);
        flashcard.setBackContent(backContent);
        flashcard.setDeckId(deckId);
        flashcardRepository.save(flashcard);
        return flashcard;
    }

    // Update existing flashcard
    public void updateFlashcard(String id, String frontContent, String backContent) {
        Optional<Flashcard> optionalFlashcard = flashcardRepository.findById(id);
        if (optionalFlashcard.isPresent()) {
            Flashcard flashcard = optionalFlashcard.get();
            flashcard.setFrontContent(frontContent);
            flashcard.setBackContent(backContent);
            flashcardRepository.save(flashcard);
        }
    }

    // Delete flashcard
    public void deleteFlashcard(String id) {
        flashcardRepository.deleteById(id);
    }

    // Get flashcard by ID
    public Flashcard getFlashcardById(String id) {
        return flashcardRepository.findById(id).orElse(null);
    }

    // Get all flashcards
    public List<Flashcard> getAllFlashcards() {
        return (List<Flashcard>) flashcardRepository.findAll();
    }
}
