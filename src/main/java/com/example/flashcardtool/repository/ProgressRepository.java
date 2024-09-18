package com.example.flashcardtool.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.example.flashcardtool.model.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgressRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Progress> findAll() {
        return dynamoDBMapper.scan(Progress.class, new DynamoDBScanExpression());
    }

    public Progress findByStudentId(String studentId) {
        return dynamoDBMapper.load(Progress.class, studentId);
    }

    public Progress save(Progress progress) {
        dynamoDBMapper.save(progress);
        return progress;
    }

    public void delete(Progress progress) {
        dynamoDBMapper.delete(progress);
    }
}
