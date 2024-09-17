package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Deck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckRepository extends CrudRepository<Deck, String> {

    List<Deck> findAll();

    List<Deck> findByUserId(String userId);

}