package apptive.team5.diary.dto;

public record DiaryUpdateRequest(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content
) {
}
