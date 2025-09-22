package apptive.team5.diary.dto;

public record DiaryUpdateDto(
        String musicTitle,
        String artist,
        String albumImageUrl,
        String videoUrl,
        String content
) {
}
