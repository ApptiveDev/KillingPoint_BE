package apptive.team5.diary.dto;

import jakarta.validation.constraints.NotEmpty;

public record DiaryRequest(
        @NotEmpty
        String artist,
        @NotEmpty
        String musicTitle,
        @NotEmpty
        String albumImageUrl,
        @NotEmpty
        String content
) {
}
