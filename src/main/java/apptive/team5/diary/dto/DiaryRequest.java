package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.user.domain.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DiaryRequest(
        @NotBlank(message = "아티스트는 필수 입력입니다.")
        String artist,
        @NotBlank(message = "음악 제목은 필수 입력입니다.")
        String musicTitle,
        @NotBlank(message = "앨범 이미지 URL은 비워둘 수 없습니다.")
        String albumImageUrl,
        @NotBlank(message = "내용을 입력해주세요")
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
