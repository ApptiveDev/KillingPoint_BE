package apptive.team5.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserTagUpdateRequest(

        @Size(min = 4, max = 30, message = "tag는 4자 이상 30자 이하이어야 합니다.")
        @Pattern(
                regexp = "^(?!.*\\.\\.)(?!\\.)[a-z0-9._]+(?<!\\.)$",
                message = "영문 소문자, 숫자, '_', '.'만 사용 가능하며, '.'으로 시작·끝낼 수 없고 연속 사용 불가합니다."
        )
        String tag
) {
}
