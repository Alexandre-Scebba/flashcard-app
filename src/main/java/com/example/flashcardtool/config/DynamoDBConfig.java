package com.example.flashcardtool.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.model.Deck;
import com.example.flashcardtool.model.StudentLibrary;
import com.example.flashcardtool.model.Progress;

import java.util.List;

@Configuration
public class DynamoDBConfig {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Bean
    @Primary
    @Qualifier("dynamoDBMapper")
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        // Create credentials using the values from application.properties
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretKey);

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createTables() {
        AmazonDynamoDB dynamoDB = amazonDynamoDB();
        DynamoDBMapper mapper = dynamoDBMapper();

        // Check if Flashcards Table exists
        if (!tableExists("Flashcards", dynamoDB)) {
            CreateTableRequest flashcardsTableRequest = mapper.generateCreateTableRequest(Flashcard.class)
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(flashcardsTableRequest);
            System.out.println("Flashcards table created.");
        } else {
            System.out.println("Flashcards table already exists.");
        }

        // Check if Decks Table exists
        if (!tableExists("Decks", dynamoDB)) {
            CreateTableRequest decksTableRequest = mapper.generateCreateTableRequest(Deck.class)
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(decksTableRequest);
            System.out.println("Decks table created.");
        } else {
            System.out.println("Decks table already exists.");
        }

        // Check if Progress Table exists
        if (!tableExists("Progress", dynamoDB)) {
            CreateTableRequest progressTableRequest = mapper.generateCreateTableRequest(Progress.class)
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

            dynamoDB.createTable(progressTableRequest);
            System.out.println("Progress table created.");
        } else {
            System.out.println("Progress table already exists.");
        }
    }

    private boolean tableExists(String tableName, AmazonDynamoDB dynamoDB) {
        ListTablesRequest request = new ListTablesRequest();
        ListTablesResult result = dynamoDB.listTables(request);
        return result.getTableNames().contains(tableName);
    }

    public void createTableIfNotExists() {
        try {
            DynamoDBMapper mapper = dynamoDBMapper();
            AmazonDynamoDB dynamoDB = amazonDynamoDB();
            CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(StudentLibrary.class);
            dynamoDB.createTable(createTableRequest);
            System.out.println("Table created: StudentLibrary");
        } catch (ResourceInUseException e) {
            System.out.println("Table already exists.");
        }
    }
}