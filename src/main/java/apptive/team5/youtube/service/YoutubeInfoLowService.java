package apptive.team5.youtube.service;

import apptive.team5.youtube.domain.YoutubeInfo;
import apptive.team5.youtube.repository.YoutubeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class YoutubeInfoLowService {

    private final YoutubeInfoRepository youtubeInfoRepository;


    public List<YoutubeInfo> saveAll(List<YoutubeInfo> youtubeInfos) {
        return youtubeInfoRepository.saveAll(youtubeInfos);
    }

    @Transactional(readOnly = true)
    public List<YoutubeInfo> findBySpotifyId(String spotifyId) {
        return youtubeInfoRepository.findBySpotifyId(spotifyId);
    }


}
