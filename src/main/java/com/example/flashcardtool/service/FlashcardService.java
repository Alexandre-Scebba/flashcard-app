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
    public Flashcard createFlashcard(String frontContent, String backContent, String deckId, String option1, String option2, String option3, String option4) {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(UUID.randomUUID().toString());
        flashcard.setFrontContent(frontContent);
        flashcard.setBackContent(backContent);
        flashcard.setDeckId(deckId);
        flashcard.setOption1(option1);
        flashcard.setOption2(option2);
        flashcard.setOption3(option3);
        flashcard.setOption4(option4);
        flashcardRepository.save(flashcard);
        return flashcard;
    }

    // Update an existing flashcard
    public void updateFlashcard(String id, String frontContent, String backContent, String option1, String option2, String option3, String option4) {
        Optional<Flashcard> optionalFlashcard = flashcardRepository.findById(id);
        if (optionalFlashcard.isPresent()) {
            Flashcard flashcard = optionalFlashcard.get();
            flashcard.setFrontContent(frontContent);
            flashcard.setBackContent(backContent);
            flashcard.setOption1(option1);
            flashcard.setOption2(option2);
            flashcard.setOption3(option3);
            flashcard.setOption4(option4);
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

    public List<Flashcard> getFlashcardsByDeckId(String deckId) {
        List<Flashcard> flashcards = flashcardRepository.findByDeckId(deckId);
        if (flashcards == null || flashcards.isEmpty()) {
            System.out.println("No flashcards found for deck with ID: " + deckId);
        } else {
            System.out.println("Flashcards found for deck with ID: " + deckId);
            flashcards.forEach(flashcard -> System.out.println(flashcard.getFrontContent()));
        }
        return flashcards;
    }
}