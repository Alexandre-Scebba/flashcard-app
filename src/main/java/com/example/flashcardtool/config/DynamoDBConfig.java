package com.example.flashcardtool.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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
import com.example.flashcardtool.model.StudentLibrary;

import java.util.List;

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
        // Heroku'daki çevre değişkenlerinden AWS kimlik bilgilerini alın
        String accessKey = System.getenv("AKIA2RP6IIYHEW6AYFVK");
        String secretKey = System.getenv("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");
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-east-1")  // AWS bölgenizi doğru şekilde ayarlayın
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
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
