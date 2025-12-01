package apptive.team5.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SurveyCreateRequestDto(

        @NotBlank(message = "빈 값은 입력이 불가능합니다.")
        @Size(min = 1, max = 1000, message = "1자 이상 1000자 이하로 작성 가능합니다.")
        String content
) {
}
