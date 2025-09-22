package apptive.team5.diary.dto;

import jakarta.validation.constraints.NotNull;

public record DiaryDeleteRequest(
        @NotNull
        Long diaryId
) {
}
