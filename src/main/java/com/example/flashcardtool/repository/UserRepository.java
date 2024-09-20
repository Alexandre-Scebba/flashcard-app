// UserRepository.java
package com.example.flashcardtool.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private DeckRepository deckRepository;

    // Save user
    public void save(User user) {
        System.out.println("User to be saved: " + user.toString());
        dynamoDBMapper.save(user);
        System.out.println("User successfully saved: " + user.getUsername());
    }

    // Get user by ID
    public User getUserById(String userId) {
        return dynamoDBMapper.load(User.class, userId);
    }

    // Delete user
    public void delete(User user) {
        dynamoDBMapper.delete(user);
    }

    // Get user by username
    public User getUserByUsername(String username) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(username));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("username = :v1")
                .withExpressionAttributeValues(eav);

        List<User> result = dynamoDBMapper.scan(User.class, scanExpression);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    // Find user by email
    public User findByEmail(String email) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(email));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("email = :v1")
                .withExpressionAttributeValues(eav);

        List<User> result = dynamoDBMapper.scan(User.class, scanExpression);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    // Find user by password reset token
    public Optional<User> findByPasswordResetToken(String token) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(token));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("passwordResetToken = :v1")
                .withExpressionAttributeValues(eav);

        List<User> result = dynamoDBMapper.scan(User.class, scanExpression);
        if (result != null && !result.isEmpty()) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }

    // Get all users (equivalent to findAll)
    public List<User> findAll() {
        return dynamoDBMapper.scan(User.class, new DynamoDBScanExpression());
    }

    // Get all students
    public List<User> findAllStudents() {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS("ROLE_STUDENT"));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#roles", "roles");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(#roles, :v1)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(expressionAttributeNames);

        return dynamoDBMapper.scan(User.class, scanExpression);
    }

    // Delete user by ID (equivalent to deleteById)
    public void deleteById(String userId) {
        User user = dynamoDBMapper.load(User.class, userId);
        if (user != null) {
            dynamoDBMapper.delete(user);
        }
    }

    public void assignDeck(String deckId, String studentId) {
        Optional<User> userOptional = findById(studentId);
        Optional<Deck> deckOptional = deckRepository.findById(deckId);

        if (userOptional.isPresent() && deckOptional.isPresent()) {
            User user = userOptional.get();
            Deck deck = deckOptional.get();

            // If the user's assignedDeckIds list is null, initialize it
            if (user.getAssignedDeckIds() == null) {
                user.setAssignedDeckIds(new ArrayList<>());
            }

            // Add the deck ID to the user's assigned decks if it's not already there
            if (!user.getAssignedDeckIds().contains(deck.getId())) {
                user.getAssignedDeckIds().add(deck.getId());
            }

            save(user); // Save the updated user with the new assigned deck
        } else {
            throw new IllegalArgumentException("User or Deck not found");
        }
    }

    public Optional<User> findById(String studentId) {
        return Optional.ofNullable(dynamoDBMapper.load(User.class, studentId));
    }
}