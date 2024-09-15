package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    public Flashcard createFlashcard(String frontContent, String backContent, String deckId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(UUID.randomUUID().toString());
        flashcard.setFrontContent(frontContent);
        flashcard.setBackContent(backContent);
        flashcard.setDeckId(deckId);
        flashcardRepository.save(flashcard);
        return flashcard;
    }

    public void updateFlashcard(String id, String frontContent, String backContent) {
        Flashcard flashcard = flashcardRepository.findById(id);
        flashcard.setFrontContent(frontContent);
        flashcard.setBackContent(backContent);
        flashcardRepository.save(flashcard);
    }

    public void deleteFlashcard(String id) {
        flashcardRepository.deleteById(id);
    }
}
