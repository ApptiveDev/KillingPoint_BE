package apptive.team5.diary.dto;

public record DiaryRequest(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content
) {
}
