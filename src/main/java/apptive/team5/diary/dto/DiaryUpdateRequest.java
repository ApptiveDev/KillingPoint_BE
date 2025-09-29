package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryScope;

public record DiaryUpdateRequest(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String videoUrl,
        String content,
        DiaryScope scope,
        String duration,
        String start,
        String end
) {
    public static DiaryUpdateDto toUpdateDto(DiaryUpdateRequest updateRequest) {
        return new DiaryUpdateDto(
                updateRequest.musicTitle,
                updateRequest.artist,
                updateRequest.albumImageUrl,
                updateRequest.videoUrl,
                updateRequest.content,
                updateRequest.scope,
                updateRequest.duration,
                updateRequest.start,
                updateRequest.end
        );
    }
}
