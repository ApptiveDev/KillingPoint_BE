package apptive.team5.file.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record FileUploadRequest(
        @NotNull(message = "TemporalFile의 PK를 입력해주세요")
        Long id,
        @URL(message = "url형식에 맞지 않습니다.")
        @NotBlank(message = "presignedUrl은 필수 값입니다.")
        String presignedUrl
) {

}
