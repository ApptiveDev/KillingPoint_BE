package apptive.team5.file.service;

import apptive.team5.file.domain.TemporalFile;
import apptive.team5.file.dto.PresignedUrlResponse;
import apptive.team5.global.AwsProperties;
import apptive.team5.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final AwsProperties awsProperties;
    private final TemporalLowService temporalLowService;

    public PresignedUrlResponse createUploadPresignedURL() {

        String filePath = UUID.randomUUID().toString();

        PutObjectRequest uploadRequest = PutObjectRequest.builder()
                .bucket(awsProperties.bucket())
                .key(filePath)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
                r -> r.putObjectRequest(uploadRequest)
                        .signatureDuration(Duration.ofMinutes(5))
        );

        String fileUrl = presignedPutObjectRequest.url().toString();

        String fileName = S3Util.extractFileName(fileUrl);

        TemporalFile saved = temporalLowService.save(new TemporalFile(S3Util.extractFileName(fileName)));

        return new PresignedUrlResponse(saved.getId(), fileUrl);
    }

    public void deleteOrphanS3Files() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);
        List<TemporalFile> orphanFiles = temporalLowService.findOrphanFiles(cutoff);
        List<String> fileNames = orphanFiles.stream()
                .map(TemporalFile::getFileName).toList();

        deleteS3Files(fileNames);

        List<Long> temporalFileIds = orphanFiles.stream().map(TemporalFile::getId).toList();

        temporalLowService.deleteByIds(temporalFileIds);
    }

    public void deleteS3Files(List<String> fileNames) {
        fileNames.forEach(this::deleteS3File);
    }

    public void deleteS3File(String fileNames) {

        String path = S3Util.extractFileName(fileNames);
        if (path.startsWith("defaultImage")) return;

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(awsProperties.bucket())
                .key(path)
                .build();

        s3Client.deleteObject(deleteRequest);
    }
}
