package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.diary.domain.model.DiaryUpdateInfo;

public record DiaryUpdateDto(
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
    public DiaryUpdateInfo toDomainInfo() {
        return new DiaryUpdateInfo(
                musicTitle,
                artist,
                albumImageUrl,
                videoUrl,
                content,
                scope,
                duration,
                totalDuration,
                start,
                end
        );
    }
}
