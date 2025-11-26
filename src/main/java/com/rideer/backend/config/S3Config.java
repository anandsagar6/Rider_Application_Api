package com.rideer.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Value("${aws.s3.bucket:}")
    private String bucketName;

    @Value("${aws.s3.region:}")
    private String region;

    @Value("${aws.s3.access-key:}")
    private String accessKey;

    @Value("${aws.s3.secret-key:}")
    private String secretKey;

    @PostConstruct
    private void validate() {
        StringBuilder sb = new StringBuilder();
        if (bucketName.isEmpty()) sb.append(" aws.s3.bucket");
        if (region.isEmpty()) sb.append(" aws.s3.region");
        if (accessKey.isEmpty()) sb.append(" aws.s3.access-key");
        if (secretKey.isEmpty()) sb.append(" aws.s3.secret-key");
        if (sb.length() > 0) {
            throw new IllegalStateException(
                    "Missing AWS S3 configuration properties:" + sb.toString() +
                            ". Make sure these are set in application.properties or as environment variables on Render (e.g. AWS_S3_BUCKET, AWS_REGION, AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY).");
        }
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                )
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                )
                .build();
    }
}
