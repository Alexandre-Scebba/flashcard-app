package com.example.flashcardtool.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.flashcardtool.model.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    // Find progress by student ID (returns a list)
    public List<Progress> findByStudentId(String studentId) {
        // Prepare the scan expression to find all entries with the matching studentId
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":studentId", new AttributeValue().withS(studentId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("studentId = :studentId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Progress.class, scanExpression);
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
