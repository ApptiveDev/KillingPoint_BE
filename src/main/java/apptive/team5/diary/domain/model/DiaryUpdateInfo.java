package apptive.team5.diary.domain.model;

import apptive.team5.diary.domain.DiaryScope;

public record DiaryUpdateInfo(
        String musicTitle,
        String artist,
        String albumImageUrl,
        String videoUrl,
        String content,
        DiaryScope scope,
        String duration,
        String totalDuration,
        String start,
        String end
) {
}
