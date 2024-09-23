package com.example.flashcardtool.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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
    private final Path rootLocation = Paths.get("upload-dir");

    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
        try {
            Files.createDirectories(rootLocation); // Ensure the upload-dir exists
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            // Generate a unique file name with UUID
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString() + "_" + originalFilename;

            // Resolve the target file path
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();

            // Check for security issues like path traversal
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            Files.copy(file.getInputStream(), destinationFile);
            return destinationFile.toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    // Upload to S3 method
    public String uploadToS3(MultipartFile file) {
        String bucketName = "your-bucket-name"; // Replace with your bucket name
        String key = "upload-dir/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        File convertedFile = convertMultipartFileToFile(file); // Convert MultipartFile to File

        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(), Paths.get(convertedFile.getPath()));

        // Return the public URL of the file
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    // Helper method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting multipart file to file.", e);
        }
        return convertedFile;
    }
}
