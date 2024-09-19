package com.example.flashcardtool.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.flashcardtool.model.Deck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DeckListConverter implements DynamoDBTypeConverter<String, List<Deck>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<Deck> object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list of decks to JSON", e);
        }
    }

    @Override
    public List<Deck> unconvert(String object) {
        try {
            return objectMapper.readValue(object, objectMapper.getTypeFactory().constructCollectionType(List.class, Deck.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to list of decks", e);
        }
    }
}