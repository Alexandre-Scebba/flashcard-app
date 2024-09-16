package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface DeckRepository extends CrudRepository<Deck, String> {
    List<Deck> findByUserId(String userId);  // Query decks by the user who created them
}
