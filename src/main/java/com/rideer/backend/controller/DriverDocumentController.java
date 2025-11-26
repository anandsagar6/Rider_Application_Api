package com.rideer.backend.controller;

import com.rideer.backend.dto.UploadResponse;
import com.rideer.backend.model.DocumentType;
import com.rideer.backend.service.S3StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Duration;

@RestController
@RequestMapping("/api/driver")
@CrossOrigin
public class DriverDocumentController {

    private final S3StorageService s3Service;

    public DriverDocumentController(S3StorageService s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(
            @RequestParam("phone") String phone,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type
    ) {
        try {
            DocumentType docType = DocumentType.valueOf(type.toUpperCase());

            String key = s3Service.uploadDocument(file, phone, docType);

            URL url = s3Service.getPresignedUrl(key, Duration.ofMinutes(30));

            return ResponseEntity.ok(new UploadResponse(true, key, url.toString(), "Uploaded Successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UploadResponse(false, null, null, e.getMessage()));
        }
    }
}
