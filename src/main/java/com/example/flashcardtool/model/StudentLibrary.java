package com.example.flashcardtool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;

@DynamoDBTable(tableName = "StudentLibrary") // Ensure the table name matches your DynamoDB table
public class StudentLibrary {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "studentId")
    private String studentId;  // Store student ID directly

    @DynamoDBAttribute(attributeName = "deckId")
    private String deckId;  // Store deck ID directly

    // Constructor
    public StudentLibrary() {
        this.id = UUID.randomUUID().toString();  // Generate ID automatically
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}
