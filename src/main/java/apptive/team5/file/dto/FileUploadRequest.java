package apptive.team5.file.dto;

import apptive.team5.file.domain.TemporalFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record FileUploadRequest(
        @NotNull
        Long id,
        @URL(message = "url형식에 맞지 않습니다.")
        @NotBlank
        String presignedUrl
) {

}
