package com.example.flashcardtool.model;

public class ProgressDTO {
    private String deckName;
    private long studyTime; // in milliseconds

    public ProgressDTO(String deckName, long studyTime) {
        this.deckName = deckName;
        this.studyTime = studyTime;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public long getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(long studyTime) {
        this.studyTime = studyTime;
    }
}
