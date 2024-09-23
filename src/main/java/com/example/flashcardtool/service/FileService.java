package com.example.flashcardtool.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private final S3Client s3Client;

    // Read bucket name from application properties or environment variables
    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final Path rootLocation = Paths.get("upload-dir");

    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
        try {
            Files.createDirectories(rootLocation); // Ensure the upload-dir exists
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    // Upload directly to S3
    public String uploadToS3(MultipartFile file) {
        String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Upload file to S3
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            // Return the public URL of the uploaded file
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    // Helper method to convert MultipartFile to File (if needed)
    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(UUID.randomUUID().toString() + "_" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
