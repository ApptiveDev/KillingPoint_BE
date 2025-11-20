package apptive.team5.diary.dto;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.global.exception.BadRequestException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.global.util.S3Util;
import apptive.team5.user.domain.UserEntity;

import java.time.LocalDateTime;

public record FeedDiaryResponseDto(
        Long diaryId,
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content,
        String videoUrl,
        DiaryScope scope,
        String duration,
        String totalDuration,
        String start,
        String end,
        LocalDateTime createDate,
        LocalDateTime updateDate,
        boolean isLiked,
        Long likeCount,
        Long userId,
        String username,
        String tag,
        String profileImageUrl
) {

    public static String defaultContentMsg = "비공개 일기입니다.";
    public static FeedDiaryResponseDto from(DiaryEntity diary, boolean isLiked, Long likeCount, Long currentUserId, UserEntity user) {
        String contentResponse = diary.getContent();

        if (!diary.isMyDiary(currentUserId) && diary.isScopeKillingPart()) {
            contentResponse = defaultContentMsg;
        }

        if (!diary.isMyDiary(currentUserId) && diary.isScopePrivate())
            throw new BadRequestException(ExceptionCode.ACCESS_DENIED_DIARY.getDescription());

        return new FeedDiaryResponseDto(
                diary.getId(),
                diary.getArtist(),
                diary.getMusicTitle(),
                diary.getAlbumImageUrl(),
                contentResponse,
                diary.getVideoUrl(),
                diary.getScope(),
                diary.getDuration(),
                diary.getTotalDuration(),
                diary.getStart(),
                diary.getEnd(),
                diary.getCreateDateTime(),
                diary.getUpdateDateTime(),
                isLiked,
                likeCount,
                user.getId(),
                user.getUsername(),
                user.getTag(),
                S3Util.s3Url + user.getProfileImage()
        );
    }
}
