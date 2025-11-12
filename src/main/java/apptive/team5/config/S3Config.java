package apptive.team5.config;

import apptive.team5.global.AwsProperties;
import apptive.team5.global.util.S3Util;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final AwsProperties awsProperties;

    @PostConstruct
    public void initS3Util() {
        S3Util.setS3Url(awsProperties.s3Url());
    }

    @Bean
    public S3Presigner s3Presigner() {
        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(awsProperties.region()));

        builder.credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsProperties.accessKey(), awsProperties.secretKey())
        ));

        return builder.build();
    }

    @Bean
    public S3Client s3Client() {
        S3Client s3Client = S3Client.builder()
                .region(Region.of(awsProperties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsProperties.accessKey(), awsProperties.secretKey())
                ))
                .build();

        return s3Client;
    }
}
