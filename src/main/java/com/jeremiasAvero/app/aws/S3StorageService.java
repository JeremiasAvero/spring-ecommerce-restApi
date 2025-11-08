package com.jeremiasAvero.app.aws;

import com.jeremiasAvero.app.image.application.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class S3StorageService implements StorageService {
    private final S3Client s3;
    @Value("${aws.s3.bucket}") String bucket;
    @Value("${aws.s3.public-base-url}") String publicBaseUrl;

    public S3StorageService(S3Client s3) { this.s3 = s3; }

    @Override
    public String uploadProductImage(Long productId, MultipartFile file) throws IOException{
        validate(file);

        String original = Optional.ofNullable(file.getOriginalFilename()).orElse(("image"));
        String safe = original.replaceAll("[^a-zA-Z0-9._-]","_");
        String key = "public/products/%d/%s-%s".formatted(productId, UUID.randomUUID(), safe);

        PutObjectRequest req = PutObjectRequest.builder()
        	    .bucket(bucket)
        	    .key(key)
        	    .contentType(file.getContentType())
        	    .build();

        	s3.putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return publicBaseUrl + "/" + key;

    }


}
