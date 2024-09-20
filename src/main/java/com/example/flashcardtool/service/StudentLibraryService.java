package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.repository.DeckRepository;
import com.example.flashcardtool.repository.StudentLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentLibraryService {

    @Autowired
    private StudentLibraryRepository studentLibraryRepository;

    @Autowired
    private DeckRepository deckRepository;

    // Add deck to student library
    public void addLibrary(String studentId, String deckId) {
        StudentLibrary library = new StudentLibrary();
        library.setStudentId(studentId);
        library.setDeckId(deckId);
        studentLibraryRepository.save(library);
    }

    public List<StudentLibrary> getLibraryByStudent(String studentId) {
        List<StudentLibrary> library = studentLibraryRepository.findByStudentId(studentId);
        System.out.println("Found " + library.size() + " decks for student ID: " + studentId);
        return library;
    }

    // Remove deck from the student's library by studentId and deckId
    public void removeLibrary(String studentId, String deckId) {
        studentLibraryRepository.deleteByStudentIdAndDeckId(studentId, deckId);
    }

    public boolean isDeckAlreadyAssigned(String studentId, String deckId) {
        return studentLibraryRepository.existsByStudentIdAndDeckId(studentId, deckId);
    }

    public List<Deck> getAssignedDecksForStudent(String studentId) {
        List<String> deckIds = studentLibraryRepository.findDeckIdsByStudentId(studentId);
        Iterable<Deck> iterableDecks = deckRepository.findAllById(deckIds);
        return StreamSupport.stream(iterableDecks.spliterator(), false)
                            .collect(Collectors.toList());
    }

    public void assignDeckToStudent(String studentId, String deckId) {
        if (!isDeckAlreadyAssigned(studentId, deckId)) {
            addLibrary(studentId, deckId);
        }
    }
}