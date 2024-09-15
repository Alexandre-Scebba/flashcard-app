package com.example.flashcardtool.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.flashcardtool.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void save(User user) {
        System.out.println("Kaydedilecek kullanıcı: " + user.toString());
        dynamoDBMapper.save(user);
        System.out.println("Kullanıcı başarıyla kaydedildi: " + user.getUsername());
    }


    public User getUserById(String userId) {
        return dynamoDBMapper.load(User.class, userId);
    }

    public void delete(User user) {
        dynamoDBMapper.delete(user);
    }


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
}

