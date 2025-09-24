package apptive.team5.diary.dto;

public record DiaryUpdateRequest(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String videoUrl,
        String content
) {
    public static DiaryUpdateDto toUpdateDto(DiaryUpdateRequest updateRequest) {
        return new DiaryUpdateDto(
                updateRequest.musicTitle,
                updateRequest.artist,
                updateRequest.albumImageUrl,
                updateRequest.videoUrl,
                updateRequest.content
        );
    }
}
