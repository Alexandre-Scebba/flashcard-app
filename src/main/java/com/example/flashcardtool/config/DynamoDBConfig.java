package com.example.flashcardtool.config;


import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import com.example.flashcardtool.model.Flashcard;
import com.example.flashcardtool.model.Deck;


@Configuration
public class DynamoDBConfig {

    @Bean
    @Primary
    @Qualifier("dynamoDBMapper")
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createTables() {
        AmazonDynamoDB dynamoDB = amazonDynamoDB();
        DynamoDBMapper mapper = dynamoDBMapper();

        // Create Flashcards Table
        CreateTableRequest flashcardsTableRequest = mapper.generateCreateTableRequest(Flashcard.class)
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

        // Create Decks Table
        CreateTableRequest decksTableRequest = mapper.generateCreateTableRequest(Deck.class)
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));

        try {
            dynamoDB.createTable(flashcardsTableRequest);
            System.out.println("Flashcards table created.");
        } catch (ResourceInUseException e) {
            System.out.println("Flashcards table already exists.");
        }

        try {
            dynamoDB.createTable(decksTableRequest);
            System.out.println("Decks table created.");
        } catch (ResourceInUseException e) {
            System.out.println("Decks table already exists.");
        }
    }
}
