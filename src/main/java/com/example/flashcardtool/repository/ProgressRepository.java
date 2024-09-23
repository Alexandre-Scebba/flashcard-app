package com.example.flashcardtool.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.flashcardtool.model.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProgressRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    // Fetch all progress entries
    public List<Progress> findAll() {
        return dynamoDBMapper.scan(Progress.class, new DynamoDBScanExpression());
    }

    // Find progress by student ID (returns a list of both study and quiz)
    public List<Progress> findByStudentId(String studentId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":studentId", new AttributeValue().withS(studentId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("studentId = :studentId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Progress.class, scanExpression);
    }



    // Find progress by studentId and deckId
    public Progress findByStudentIdAndDeckId(String studentId, String deckId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":studentId", new AttributeValue().withS(studentId));
        eav.put(":deckId", new AttributeValue().withS(deckId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("studentId = :studentId AND deckId = :deckId")
                .withExpressionAttributeValues(eav);

        List<Progress> results = dynamoDBMapper.scan(Progress.class, scanExpression);
        return results.isEmpty() ? null : results.get(0);
    }

    // Save or update progress
    public Progress save(Progress progress) {
        dynamoDBMapper.save(progress);
        return progress;
    }

    // Delete progress entry
    public void delete(Progress progress) {
        dynamoDBMapper.delete(progress);
    }
}
