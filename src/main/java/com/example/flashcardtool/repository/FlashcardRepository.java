package com.example.flashcardtool.repository;

import com.example.flashcardtool.model.Flashcard;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface FlashcardRepository extends CrudRepository<Flashcard, String> {
}
