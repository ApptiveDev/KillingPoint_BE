package apptive.team5.youtube.repository;

import apptive.team5.youtube.domain.YoutubeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YoutubeInfoRepository extends JpaRepository<YoutubeInfo, Long> {
    List<YoutubeInfo> findBySpotifyId(String spotifyId);
}
