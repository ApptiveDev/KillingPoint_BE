package apptive.team5.file.controller;

import apptive.team5.file.dto.PresignedUrlResponse;
import apptive.team5.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presigned-url")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<PresignedUrlResponse> getPresignedURL() {
        PresignedUrlResponse response = s3Service.createUploadPresignedURL();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
