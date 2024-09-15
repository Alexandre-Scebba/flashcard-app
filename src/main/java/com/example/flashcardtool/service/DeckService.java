package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    public Deck createDeck(String name, String userId) {
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());
        deck.setName(name);
        deck.setUserId(userId);
        deckRepository.save(deck);
        return deck;
    }

    public void updateDeck(String id, String name) {
        Deck deck = deckRepository.findById(id);
        deck.setName(name);
        deckRepository.save(deck);
    }

    public void deleteDeck(String id) {
        deckRepository.deleteById(id);
    }

    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    public Deck getDeckById(String id) {
        return null;
    }
}