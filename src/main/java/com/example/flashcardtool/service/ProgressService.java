package com.example.flashcardtool.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.flashcardtool.model.Progress;
import com.example.flashcardtool.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProgressService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private ProgressRepository progressRepository;

    public void saveProgress(Progress progress) {
        // Eğer ID yoksa yeni bir ID oluştur
        if (progress.getId() == null) {
            progress.setId(UUID.randomUUID().toString());
        }
        // Progress verisini DynamoDB'ye kaydet
        dynamoDBMapper.save(progress);
        System.out.println("Progress saved for student: " + progress.getStudentId());
    }

    // Belirli bir öğrencinin ilerlemelerini getir
    public List<Progress> getStudentProgress(String studentId) {
        return progressRepository.findByStudentId(studentId);
    }


    // Öğrencinin tüm ilerlemelerini getir
    public List<Progress> getProgressByStudentId(String studentId) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":studentId", new AttributeValue().withS(studentId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("studentId = :studentId")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Progress.class, scanExpression);
    }
}
