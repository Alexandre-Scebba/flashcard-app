package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface DeckRepository extends CrudRepository<Deck, String> {

    List<Deck> findAll();

    List<Deck> findByUserId(String userId);  // Query decks by the user who created them

    List<Deck> findByNameContaining(String name);  // Add this method to search decks by name containing a keyword

    Optional<Deck> findByName(String name);  // Add this method to find a deck by its exact name
}
