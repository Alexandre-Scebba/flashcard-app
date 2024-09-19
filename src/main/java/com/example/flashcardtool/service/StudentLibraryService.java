package com.example.flashcardtool.service;

import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.repository.StudentLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentLibraryService {

    @Autowired
    private StudentLibraryRepository studentLibraryRepository;

    // Add deck to student library
    public void addLibrary(String studentId, String deckId) {
        StudentLibrary library = new StudentLibrary();
        library.setStudentId(studentId);
        library.setDeckId(deckId);
        studentLibraryRepository.save(library); // Save to the repository
    }

    // Fetch decks in the student library
    public List<StudentLibrary> getLibraryByStudent(String studentId) {
        return studentLibraryRepository.findByStudentId(studentId);
    }

    // Remove deck from the student's library by studentId and deckId
    public void removeLibrary(String studentId, String deckId) {
        studentLibraryRepository.deleteByStudentIdAndDeckId(studentId, deckId);
    }
}
