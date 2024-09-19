package com.example.flashcardtool.model;
import java.util.List;

public class AssignmentRequest {

    private String deckId;
    private List<String> studentIds;

    // Getters and setters
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
}