package com.example.flashcardtool.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.flashcardtool.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void save(Deck deck) {
        dynamoDBMapper.save(deck);
    }

    public Deck findById(String deckId) {
        return dynamoDBMapper.load(Deck.class, deckId);
    }

    public void delete(Deck deck) {
        dynamoDBMapper.delete(deck);
    }


    public List<Deck> findAll() {
        return dynamoDBMapper.scan(Deck.class, new DynamoDBScanExpression());
    }


}
