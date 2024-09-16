package com.example.flashcardtool;

import org.springframework.boot.SpringApplication;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDynamoDBRepositories(basePackages = "com.example.flashcardtool.repository")
public class FlashcardtoolApplication {

	public static void main(String[] args) {

		SpringApplication.run(FlashcardtoolApplication.class, args);
	}

}
