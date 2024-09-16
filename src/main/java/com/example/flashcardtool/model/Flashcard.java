package com.example.flashcardtool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Flashcards")
public class Flashcard {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String frontContent;

    @DynamoDBAttribute
    private String backContent;

    @DynamoDBAttribute
    private String deckId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontContent() {
        return frontContent;
    }

    public void setFrontContent(String frontContent) {
        this.frontContent = frontContent;
    }

    public String getBackContent() {
        return backContent;
    }

    public void setBackContent(String backContent) {
        this.backContent = backContent;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}
