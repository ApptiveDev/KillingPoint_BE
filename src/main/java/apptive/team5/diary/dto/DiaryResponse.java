package apptive.team5.diary.dto;


import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import java.time.LocalDateTime;

public record DiaryResponse(
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
        LocalDateTime updateDate
) {
    public static DiaryResponse from(DiaryEntity diary) {
        return new DiaryResponse(
                diary.getArtist(),
                diary.getMusicTitle(),
                diary.getAlbumImageUrl(),
                diary.getContent(),
                diary.getVideoUrl(),
                diary.getScope(),
                diary.getDuration(),
                diary.getStart(),
                diary.getEnd(),
                diary.getCreateDateTime(),
                diary.getUpdateDateTime()
        );
    }
}
