package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Optional<Deck> optionalDeck = deckRepository.findById(id);
        if (optionalDeck.isPresent()) {
            Deck deck = optionalDeck.get();  // Get the Deck object from Optional
            deck.setName(name);
            deckRepository.save(deck);  // Save the updated Deck object
        } else {
            // Handle the case where the deck is not found (e.g., throw an exception or log a message)
            System.out.println("Deck not found with id: " + id);
        }
    }

    public void deleteDeck(String id) {
        deckRepository.deleteById(id);
    }

    public List<Deck> getAllDecks() {
        return deckRepository.findAll();  // Ensure this method exists in your repository
    }

    public Optional<Deck> getDeckById(String id) {
        return deckRepository.findById(id);
    }


    public List<Deck> getDecksByUserId(String userId) {
        return deckRepository.findByUserId(userId);
    }
}
