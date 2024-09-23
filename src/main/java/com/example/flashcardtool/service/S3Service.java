package com.example.flashcardtool.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region,
                     @Value("${aws.s3.bucket}") String bucketName) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String region = System.getenv("AWS_REGION");  // Fetch the region from environment variables
        String url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }


}
