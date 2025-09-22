package apptive.team5.diary.dto;


import apptive.team5.diary.domain.DiaryEntity;

public record DiaryResponse(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content,
        String videoUrl
) {
    public static DiaryResponse from(DiaryEntity diary) {
        return new DiaryResponse(
                diary.getArtist(),
                diary.getMusicTitle(),
                diary.getAlbumImageUrl(),
                diary.getContent(),
                diary.getVideoUrl()
        );
    }
}
