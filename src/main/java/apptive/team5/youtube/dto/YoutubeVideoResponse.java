package apptive.team5.youtube.dto;

import com.google.api.services.youtube.model.Video;

public record YoutubeVideoResponse (
        String title,
        String duration,
        String url
){

    public YoutubeVideoResponse(Video video) {
        this(video.getSnippet().getTitle(), video.getContentDetails().getDuration(),
                "https://www.youtube-nocookie.com/embed/" + video.getId());
    }
}
