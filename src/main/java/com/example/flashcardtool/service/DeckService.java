package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    // Create a new deck with the given name and userId
    public Deck createDeck(String name, String userId) {
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());
        deck.setName(name);
        deck.setUserId(userId);
        deckRepository.save(deck);
        return deck;
    }

    // Update an existing deck by ID
    public void updateDeck(String id, String name) {
        Optional<Deck> optionalDeck = deckRepository.findById(id);
        if (optionalDeck.isPresent()) {
            Deck deck = optionalDeck.get();
            deck.setName(name);
            deckRepository.save(deck);
        }
    }

    // Delete a deck by ID
    public void deleteDeck(String id) {
        deckRepository.deleteById(id);
    }

    // Retrieve all decks
    public List<Deck> getAllDecks() {
        List<Deck> deckList = new ArrayList<>();
        deckRepository.findAll().forEach(deckList::add);
        return deckList;
    }

    // Get a deck by its ID
    public Optional<Deck> getDeckById(String id) {
        return deckRepository.findById(id);
    }
}
