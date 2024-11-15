package com.example.flashcardtool.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.model.User;
import com.example.flashcardtool.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentLibraryService {

    @Autowired
    private AmazonDynamoDB dynamoDB;

    private final String TABLE_NAME = "StudentLibrary";

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserService userService;

    public void addLibrary(String studentId, String deckId) {
        if (studentId != null && deckId != null) {
            // Burada kullanıcıyı StudentID'yi almak için bir kontrol yapıyoruz
            Optional<User> userOptional = userService.findById(studentId);  // findByUsername yerine findById kullanıyoruz
            if (userOptional.isPresent()) {
                String actualStudentId = userOptional.get().getId(); // Gerçek StudentID

                Map<String, AttributeValue> itemValues = new HashMap<>();
                itemValues.put("StudentID", new AttributeValue().withS(actualStudentId));  // Correct StudentID kullanımı
                itemValues.put("deckId", new AttributeValue().withS(deckId));        // Store deckId
                itemValues.put("id", new AttributeValue().withS(UUID.randomUUID().toString())); // Create a unique ID

                dynamoDB.putItem(TABLE_NAME, itemValues);
            } else {
                System.out.println("User not found for studentId: " + studentId);
            }
        } else {
            System.out.println("Student ID or Deck ID is null. Cannot add to library.");
        }
    }

    // Retrieve the library by student ID
    public List<StudentLibrary> getLibraryByStudent(String studentId) {
        if (studentId != null) {
            Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
            expressionAttributeValues.put(":studentId", new AttributeValue().withS(studentId));

            // AWS üzerinde StudentID kullanılarak sorgu yapılıyor
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
            // Burada studentId'nin gerçekten User tablosundaki ID olduğundan emin olun
            Optional<User> userOptional = userService.findByUsername(studentId); // findByUsername yerine findById kullanılabilir
            if (userOptional.isPresent()) {
                String actualStudentId = userOptional.get().getId();  // Gerçek StudentID alınır

                Map<String, AttributeValue> key = new HashMap<>();
                key.put("StudentID", new AttributeValue().withS(actualStudentId));  // Partition Key
                key.put("deckId", new AttributeValue().withS(deckId));              // Sort Key

                // Log ekleyin: StudentID ve DeckID'yi kontrol etmek için
                System.out.println("Removing deck with actualStudentId: " + actualStudentId + " and deckId: " + deckId);

                dynamoDB.deleteItem(TABLE_NAME, key);
                System.out.println("Deck removed successfully from the library.");
            } else {
                System.out.println("User not found for username: " + studentId);
            }
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
