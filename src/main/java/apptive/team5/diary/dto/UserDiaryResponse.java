package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.user.domain.UserEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public record UserDiaryResponse(
        Long diaryId,
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content,
        String videoUrl,
        DiaryScope scope,
        String duration,
        String start,
        String end,
        LocalDateTime createDate,
        LocalDateTime updateDate,
        boolean isLiked
) {
    public static String defaultContentMsg = "비공개 일기입니다";
    public static UserDiaryResponse from(DiaryEntity diary, boolean isLiked, UserEntity currentUser) {
        String contentResponse = diary.getContent();

        if (!diary.isMyDiary(currentUser) &&
            diary.isScopeKillingPart()) {
            contentResponse = defaultContentMsg;
        }

        return new UserDiaryResponse(
                diary.getId(),
                diary.getArtist(),
                diary.getMusicTitle(),
                diary.getAlbumImageUrl(),
                contentResponse,
                diary.getVideoUrl(),
                diary.getScope(),
                diary.getDuration(),
                diary.getStart(),
                diary.getEnd(),
                diary.getCreateDateTime(),
                diary.getUpdateDateTime(),
                isLiked
        );
    }
}
