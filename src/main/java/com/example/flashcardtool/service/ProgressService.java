package com.example.flashcardtool.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.flashcardtool.model.Progress;
import com.example.flashcardtool.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProgressService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private ProgressRepository progressRepository;

    // Save progress for either a study or quiz session
    public void saveProgress(Progress progress) {
        // If the ID is null, generate a new one
        if (progress.getId() == null) {
            progress.setId(UUID.randomUUID().toString());
        }
        // Save progress in DynamoDB
        dynamoDBMapper.save(progress);
        System.out.println("Progress saved for student: " + progress.getStudentId());
    }

    // Fetch all study progress by student ID
    public List<Progress> getStudyProgressByStudentId(String studentId) {
        return progressRepository.findByStudentIdAndType(studentId, "study");
    }

    // Fetch all quiz progress by student ID
    public List<Progress> getQuizProgressByStudentId(String studentId) {
        return progressRepository.findByStudentIdAndType(studentId, "quiz");
    }

    // Fetch all progress for a student (both study and quiz)
    public List<Progress> getProgressByStudentId(String studentId) {
        return progressRepository.findByStudentId(studentId);
    }
}
