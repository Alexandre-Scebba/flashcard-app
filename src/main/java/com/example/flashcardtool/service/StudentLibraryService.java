package com.example.flashcardtool.service;

import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.repository.DeckRepository;
import com.example.flashcardtool.repository.StudentLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        if (studentId != null && deckId != null) {
            StudentLibrary library = new StudentLibrary();
            library.setStudentId(studentId);
            library.setDeckId(deckId);
            studentLibraryRepository.save(library);
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot add to library.");
        }
    }

    public List<StudentLibrary> getLibraryByStudent(String studentId) {
        if (studentId != null) {
            List<StudentLibrary> library = studentLibraryRepository.findByStudentId(studentId);
            System.out.println("Found " + library.size() + " decks for student ID: " + studentId);
            return library;
        } else {
            System.out.println("Student ID is null. Cannot retrieve library.");
            return null;
        }
    }

    // Remove deck from the student's library by studentId and deckId
    public void removeLibrary(String studentId, String deckId) {
        if (studentId != null && deckId != null) {
            studentLibraryRepository.deleteByStudentIdAndDeckId(studentId, deckId);
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot remove from library.");
        }
    }

    public boolean isDeckAlreadyAssigned(String studentId, String deckId) {
        if (studentId != null && deckId != null) {
            return studentLibraryRepository.existsByStudentIdAndDeckId(studentId, deckId);
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot check assignment.");
            return false;
        }
    }

    public List<Deck> getAssignedDecksForStudent(String studentId) {
        if (studentId != null) {
            List<String> deckIds = studentLibraryRepository.findDeckIdsByStudentId(studentId);
            Iterable<Deck> iterableDecks = deckRepository.findAllById(deckIds);
            return StreamSupport.stream(iterableDecks.spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            System.out.println("Student ID is null. Cannot retrieve assigned decks.");
            return null;
        }
    }

    public void assignDeckToStudent(String studentId, String deckId) {
        if (!isDeckAlreadyAssigned(studentId, deckId)) {
            addLibrary(studentId, deckId);
        }
    }
}
