package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface DeckRepository extends CrudRepository<Deck, String> {
    Optional<Deck> findByName(String name);  // Query deck by its name
    List<Deck> findByNameContaining(String name);
}

