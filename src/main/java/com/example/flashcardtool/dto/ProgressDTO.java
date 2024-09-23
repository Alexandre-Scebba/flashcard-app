package com.example.flashcardtool.dto;

public class ProgressDTO {
    private String deckName;
    private long studyTime;
    private int correctAnswers;
    private int incorrectAnswers;

    public ProgressDTO(String deckName, long studyTime, int correctAnswers, int incorrectAnswers) {
        this.deckName = deckName;
        this.studyTime = studyTime;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
    }

    public String getDeckName() {
        return deckName;
    }

    public long getStudyTime() {
        return studyTime;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }
}
