package com.example.flashcardtool.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentLibraryService {

    @Autowired
    private AmazonDynamoDB dynamoDB;

    private final String TABLE_NAME = "StudentLibrary";

    @Autowired
    private DeckRepository deckRepository;

    // Add deck to student library
    public void addLibrary(String studentId, String deckId) {
        if (studentId != null && deckId != null) {
            // DynamoDB için öğeyi oluşturuyoruz
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("StudentID", new AttributeValue().withS(studentId));  // Partition Key
            item.put("deckId", new AttributeValue().withS(deckId));        // Sort Key
            item.put("id", new AttributeValue().withS(UUID.randomUUID().toString()));  // Ekstra bir ID alanı

            // DynamoDB'ye veriyi ekliyoruz
            PutItemRequest putItemRequest = new PutItemRequest()
                    .withTableName("StudentLibrary")
                    .withItem(item);

            dynamoDB.putItem(putItemRequest);
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot add to library.");
        }
    }


    // Retrieve the library by student ID
    public List<StudentLibrary> getLibraryByStudent(String studentId) {
        if (studentId != null) {
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":studentId", new AttributeValue().withS(studentId));

            ScanRequest scanRequest = new ScanRequest()
                    .withTableName("StudentLibrary")
                    .withFilterExpression("StudentID = :studentId")
                    .withExpressionAttributeValues(expressionAttributeValues);

            // DynamoDB'den veri çekme ve null kontrolü
            ScanResult result = dynamoDB.scan(scanRequest);
            List<StudentLibrary> library = result.getItems().stream()
                    .map(item -> {
                        String studentIdValue = item.containsKey("StudentID") ? item.get("StudentID").getS() : null;
                        String deckIdValue = item.containsKey("deckId") ? item.get("deckId").getS() : null;
                        String idValue = item.containsKey("id") ? item.get("id").getS() : null;
                        return new StudentLibrary(studentIdValue, deckIdValue, idValue);
                    })
                    .collect(Collectors.toList());

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
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("StudentID", new AttributeValue().withS(studentId));  // Partition Key
            key.put("deckId", new AttributeValue().withS(deckId));        // Sort Key (dikkat: küçük harfli)

            dynamoDB.deleteItem(TABLE_NAME, key);
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot remove from library.");
        }
    }


    // Check if a deck is already assigned to the student
    public boolean isDeckAlreadyAssigned(String studentId, String deckId) {
        if (studentId != null && deckId != null) {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("StudentID", new AttributeValue().withS(studentId));
            key.put("DeckID", new AttributeValue().withS(deckId));

            GetItemRequest getItemRequest = new GetItemRequest()
                    .withTableName(TABLE_NAME)
                    .withKey(key);

            return dynamoDB.getItem(getItemRequest).getItem() != null;
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot check assignment.");
            return false;
        }
    }

    // Retrieve assigned decks for a student
    public List<Deck> getAssignedDecksForStudent(String studentId) {
        if (studentId != null) {
            List<StudentLibrary> studentLibraries = getLibraryByStudent(studentId);
            List<String> deckIds = studentLibraries.stream()
                    .map(StudentLibrary::getDeckId)
                    .collect(Collectors.toList());

            Iterable<Deck> iterableDecks = deckRepository.findAllById(deckIds);
            return StreamSupport.stream(iterableDecks.spliterator(), false)
                    .collect(Collectors.toList());
        } else {
            System.out.println("Student ID is null. Cannot retrieve assigned decks.");
            return null;
        }
    }

    // Assign a deck to the student if not already assigned
    public void assignDeckToStudent(String studentId, String deckId) {
        if (!isDeckAlreadyAssigned(studentId, deckId)) {
            addLibrary(studentId, deckId);
        }
    }
}
