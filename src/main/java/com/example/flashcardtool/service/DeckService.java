package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.repository.DeckRepository;
import com.example.flashcardtool.repository.FlashcardRepository;
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

    @Autowired
    private FlashcardRepository flashcardRepository;

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

    // Retrieve all flashcards for a specific deck
    public List<Flashcard> getFlashcardsForDeck(String deckId) {
        return flashcardRepository.findByDeckId(deckId);  // Find flashcards by deck ID
    }

    // Get a deck by its ID
    public Optional<Deck> getDeckById(String deckId) {
        Optional<Deck> deck = deckRepository.findById(deckId);
        System.out.println("Deck ID: " + deckId + " found: " + deck.isPresent());
        return deck;
    }

    public Deck save(Deck deck) {
        if (deck.getId() == null || deck.getId().isEmpty()) {
            deck.setId(UUID.randomUUID().toString());  // Ensure the ID is set
        }
        return deckRepository.save(deck);
    }

    // Search decks by name containing a search term
    public List<Deck> searchDecksByName(String deckName) {
        return deckRepository.findByNameContaining(deckName);
    }

    // Find deck by its name
    public Optional<Deck> findByName(String name) {
        return deckRepository.findByName(name);
    }
<<<<<<< Updated upstream
}
=======
    public void assignDeck(String deckId, String studentId, String teacherName) {
        Optional<User> studentOpt = userRepository.findById(studentId); // Öğrenciyi bul

        if (studentOpt.isPresent()) {
            User student = studentOpt.get();

            // Eğer deck daha önce atanmadıysa, assignedDeckIds listesine ekle
            if (!student.getAssignedDeckIds().contains(deckId)) {
                student.getAssignedDeckIds().add(deckId); // Sadece Deck ID'yi ekle
                userRepository.save(student); // Öğrenciyi kaydet
                System.out.println("Deck başarıyla öğrenciye atandı.");
            } else {
                System.out.println("Deck zaten öğrenciye atanmış.");
            }
        } else {
            System.out.println("Öğrenci bulunamadı.");
        }
    }


}
>>>>>>> Stashed changes
