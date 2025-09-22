package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.user.domain.UserEntity;
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
        public static DiaryEntity toEntity(DiaryRequest diaryRequest, String videoUrl, UserEntity user) {
                return new DiaryEntity(
                        diaryRequest.musicTitle,
                        diaryRequest.artist,
                        diaryRequest.albumImageUrl,
                        videoUrl,
                        diaryRequest.content(),
                        user
                );
        }
}
