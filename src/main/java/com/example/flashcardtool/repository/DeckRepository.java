package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository {
    Deck findById(String id);

    void save(Deck deck);

    void deleteById(String id);

    List<Deck> findAll();
}