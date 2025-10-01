package apptive.team5.youtube.dto;

import com.google.api.services.youtube.model.Video;

public record YoutubeVideoResponse (
        String title,
        String duration,
        String url
) implements Comparable<YoutubeVideoResponse> {

    public YoutubeVideoResponse(Video video) {
        this(video.getSnippet().getTitle(), video.getContentDetails().getDuration(),
                "https://www.youtube-nocookie.com/embed/" + video.getId());
    }

    @Override
    public int compareTo(YoutubeVideoResponse other) {
        return Integer.compare(priority(this.title), priority(other.title));
    }

    private int priority(String title) {
        if (title.contains("Official Audio")) return 1;
        if (title.contains("Lyrics")) return 2;
        if (title.contains("가사")) return 3;
        if (title.contains("Official MV")) return 4;
        return 5;
    }
}
