package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryScope;

public record DiaryUpdateDto(
        String musicTitle,
        String artist,
        String albumImageUrl,
        String videoUrl,
        String content,
        DiaryScope scope,
        String duration,
        String start,
        String end
) {
}
