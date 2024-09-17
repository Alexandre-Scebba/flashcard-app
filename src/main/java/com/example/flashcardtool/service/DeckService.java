package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    public Deck createDeck(String name, String userId, String description) {
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());  // Ensure the ID is set
        deck.setName(name);
        deck.setUserId(userId);
        deck.setDescription(description);
        return deckRepository.save(deck);  // Save the deck with a generated ID
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


    public Deck save(Deck deck) {
        if (deck.getId() == null || deck.getId().isEmpty()) {
            deck.setId(UUID.randomUUID().toString());  // Ensure the ID is set
        }
        return deckRepository.save(deck);
    }

    // Find all method, converting Iterable to List
    public List<Deck> findAll() {
        return StreamSupport.stream(deckRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}