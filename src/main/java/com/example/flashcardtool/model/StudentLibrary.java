package com.example.flashcardtool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.UUID;

@DynamoDBTable(tableName = "StudentLibrary")
public class StudentLibrary {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    @DynamoDBAttribute(attributeName = "studentId")
    private String studentId;

    @DynamoDBAttribute(attributeName = "deckId")
    private String deckId;

    // Parametresiz yapıcı (default constructor)
    public StudentLibrary() {
        this.id = UUID.randomUUID().toString();
    }

    // Parametreli yapıcı
    public StudentLibrary(String studentId, String deckId) {
        this.studentId = studentId;
        this.deckId = deckId;
        this.id = UUID.randomUUID().toString(); // Eğer id sağlanmazsa, otomatik oluşturulur
    }

    // Parametreli yapıcı (id ile birlikte)
    public StudentLibrary(String studentId, String deckId, String id) {
        this.studentId = studentId;
        this.deckId = deckId;
        this.id = id != null ? id : UUID.randomUUID().toString(); // Eğer id null ise, UUID ile oluştur
    }

    // Getter ve Setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }
}
