package apptive.team5.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record AwsProperties(
        @Value("${aws.credentials.access-key}")
        String accessKey,
        @Value("${aws.credentials.secret-key}")
        String secretKey,
        @Value("${aws.s3.region}")
        String region,
        @Value("${aws.s3.bucket}")
        String bucket,
        @Value("${aws.s3.url}")
        String s3Url
) {
}
