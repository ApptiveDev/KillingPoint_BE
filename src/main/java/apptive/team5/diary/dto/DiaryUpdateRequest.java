package apptive.team5.diary.dto;

public record DiaryUpdateRequest(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content
) {
    public static DiaryUpdateDto toUpdateDto(DiaryUpdateRequest updateRequest, String videoUrl) {
        return new DiaryUpdateDto(
                updateRequest.musicTitle,
                updateRequest.artist,
                updateRequest.albumImageUrl,
                videoUrl,
                updateRequest.content
        );
    }
}
