package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface DeckRepository extends CrudRepository<Deck, String> {

    Optional<Deck> findByName(String name);  // Query by deck name

    List<Deck> findByNameContaining(String name);  // Search deck names containing a string

    // Ek olarak öğrenciye atanan desteleri sorgulamak için yardımcı bir metot ekleyebiliriz
    List<Deck> findByAssignedStudentId(String studentId);  // Query by student ID (assigned decks)
}
