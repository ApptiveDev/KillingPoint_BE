package apptive.team5.youtube.service;

import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.global.exception.ExternalApiConnectException;
import apptive.team5.youtube.YoutubeApiKeyProvider;
import apptive.team5.youtube.domain.YoutubeInfo;
import apptive.team5.youtube.dto.YoutubeSearchRequest;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class YoutubeService {

    private final YoutubeApiKeyProvider apiKeyProvider;
    private static final GsonFactory gsonFactory = new GsonFactory();
    private final YoutubeInfoLowService youtubeInfoLowService;

    public List<YoutubeVideoResponse> searchVideo(YoutubeSearchRequest searchRequest) {

        List<YoutubeInfo> findYoutubeInfo = youtubeInfoLowService.findBySpotifyId(searchRequest.spotifyId());

        if (!findYoutubeInfo.isEmpty()) {
            return findYoutubeInfo.stream()
                    .map(YoutubeVideoResponse::new)
                    .sorted()
                    .toList();
        }

        String apiKey = apiKeyProvider.nextKey();

        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(), gsonFactory, request -> {}
        ).build();

        try {
            SearchListResponse searchResponse = youtube.search()
                    .list(Collections.singletonList("id,snippet"))
                    .setQ(searchRequest.artist() + " " + searchRequest.title())
                    .setType(Collections.singletonList("video"))
                    .setMaxResults(5L)
                    .setKey(apiKey)
                    .execute();

            List<String> videoIds = searchResponse.getItems()
                    .stream()
                    .map(r -> r.getId().getVideoId())
                    .filter(Objects::nonNull)
                    .toList();

            VideoListResponse videoListResponse = youtube.videos()
                    .list(Collections.singletonList("snippet,contentDetails,statistics"))
                    .setId(Collections.singletonList(String.join(",", videoIds)))
                    .setKey(apiKey)
                    .execute();

            if(videoListResponse.isEmpty()) return List.of();

            List<YoutubeVideoResponse> youtubeVideoResponses = videoListResponse.getItems()
                    .stream()
                    .map(YoutubeVideoResponse::new)
                    .sorted()
                    .toList();

            List<YoutubeInfo> youtubeInfos = youtubeVideoResponses.
                    stream()
                    .map(videoResponse -> new YoutubeInfo(searchRequest.spotifyId(), videoResponse))
                    .toList();

            youtubeInfoLowService.saveAll(youtubeInfos);

            return youtubeVideoResponses;

        } catch (IOException e) {
            throw new ExternalApiConnectException(
                    ExceptionCode.YOUTUBE_API_EXCEPTION.getDescription(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
