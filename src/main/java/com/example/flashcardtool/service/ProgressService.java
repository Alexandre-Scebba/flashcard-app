package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Progress;
import com.example.flashcardtool.repository.ProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;

    public ProgressService(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    // Fetch progress for a specific student
    public List<Progress> getStudentProgress(String studentId) {
        return (List<Progress>) progressRepository.findByStudentId(studentId);
    }

    // Save or update student progress
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }
}
