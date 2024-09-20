package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.DeckRepository;
import com.example.flashcardtool.repository.FlashcardRepository;
import com.example.flashcardtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    // Create a new deck
    public Deck createDeck(String name, String userId, String description) {
        Deck deck = new Deck();
        deck.setId(UUID.randomUUID().toString());
        deck.setName(name);
        deck.setUserId(userId);
        deck.setDescription(description);
        return deckRepository.save(deck);
    }

    // Update an existing deck by ID
    public void updateDeck(String id, String name) {
        deckRepository.findById(id).ifPresent(deck -> {
            deck.setName(name);
            deckRepository.save(deck);
        });
    }

    // Delete a deck by ID
    public void deleteDeck(String id) {
        deckRepository.deleteById(id);
    }

    // Retrieve all decks
    public List<Deck> getAllDecks() {
        return StreamSupport.stream(deckRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // Retrieve all flashcards for a specific deck
    public List<Flashcard> getFlashcardsForDeck(String deckId) {
        return flashcardRepository.findByDeckId(deckId);
    }

    // Get a deck by its ID
    public Optional<Deck> getDeckById(String deckId) {
        return deckRepository.findById(deckId);
    }

    // Save a deck
    public void save(Deck deck) {
        if (deck.getId() == null || deck.getId().isEmpty()) {
            deck.setId(UUID.randomUUID().toString());
        }
        deckRepository.save(deck);
    }

    // Search decks by name containing a search term
    public List<Deck> searchDecksByName(String deckName) {
        return deckRepository.findByNameContaining(deckName);
    }

    // Find a deck by its name
    public Optional<Deck> findByName(String name) {
        return deckRepository.findByName(name);
    }

    // Assign a deck to a student
    public void assignDeck(String deckId, String studentId) {
        Optional<Deck> deckOpt = deckRepository.findById(deckId);
        Optional<User> studentOpt = userRepository.findById(studentId); // Use instance method

        if (deckOpt.isPresent() && studentOpt.isPresent()) {
            Deck deck = deckOpt.get();
            User student = studentOpt.get();
            // ensure the collection is modifiable
            if (student.getAssignedDecks().getClass() == Collections.unmodifiableList(Collections.emptyList()).getClass()) {
                student.setAssignedDecks(new ArrayList<>(student.getAssignedDecks()));
            }
            student.getAssignedDecks().add(deck);
            userRepository.save(student); // Use instance method
        }
    }
}