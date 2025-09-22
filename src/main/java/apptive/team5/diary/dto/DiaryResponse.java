package apptive.team5.diary.dto;


public record DiaryResponse(
        String artist,
        String musicTitle,
        String albumImageUrl,
        String content,
        String videoUrl
) {
}
