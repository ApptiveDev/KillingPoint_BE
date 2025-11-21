package apptive.team5.youtube.domain;

import apptive.team5.youtube.dto.YoutubeVideoResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class YoutubeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String spotifyId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private String url;


    public YoutubeInfo(String spotifyId, YoutubeVideoResponse youtubeVideoResponse) {
        this.spotifyId = spotifyId;
        this.title = youtubeVideoResponse.title();
        this.duration = youtubeVideoResponse.duration();
        this.url = youtubeVideoResponse.url();
    }

}
