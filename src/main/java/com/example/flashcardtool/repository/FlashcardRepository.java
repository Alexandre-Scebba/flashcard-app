package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Flashcard;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository {
    Flashcard findById(String id);
    void save(Flashcard flashcard);
    void deleteById(String id);
}
