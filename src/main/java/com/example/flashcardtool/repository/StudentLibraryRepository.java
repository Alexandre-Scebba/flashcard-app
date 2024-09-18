package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.StudentLibrary;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@EnableScan
public interface StudentLibraryRepository extends CrudRepository<StudentLibrary, String> {
    List<StudentLibrary> findByStudentId(String studentId);

    List<StudentLibrary> findByDeckId(String deckId);

    // Add method to delete by studentId and deckId
    void deleteByStudentIdAndDeckId(String studentId, String deckId);

}