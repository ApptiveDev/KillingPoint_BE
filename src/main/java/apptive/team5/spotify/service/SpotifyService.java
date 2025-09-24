package apptive.team5.spotify.service;

import apptive.team5.global.exception.ExternalApiConnectException;
import apptive.team5.spotify.dto.SpotifyTrackResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyService {
    private final WebClient webClient;

    @Value("${spotify.client.id}")
    private String clientId;
    @Value("${spotify.client.secret}")
    private String clientSecret;
    @Value("${spotify.auth.url}")
    private String authUrl;
    @Value("${spotify.api.url}")
    private String apiUrl;

    private int maxSearchResults = 20;

    public Mono<List<SpotifyTrackResponseDto>> searchMusic(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("검색어가 없습니다."));
        }

        return getSpotifyAccessToken().flatMap(token ->
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(apiUrl + "/search")
                                .queryParam("q", query)
                                .queryParam("type", "track")
                                .queryParam("market", "KR")
                                .queryParam("limit", maxSearchResults)
                                .build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError,
                                response -> response.bodyToMono(String.class)
                                        .map(body -> new ExternalApiConnectException("Spotify API 4xx 에러: " + body, HttpStatus.BAD_REQUEST)))
                        .onStatus(HttpStatusCode::is5xxServerError,
                                response -> response.bodyToMono(String.class)
                                        .map(body -> new ExternalApiConnectException("Spotify API 5xx 에러: " + body, HttpStatus.BAD_GATEWAY)))
                        .bodyToMono(JsonNode.class)
                        .map(this::searchResultToTrackDto)
        );
    }

    private Mono<String> getSpotifyAccessToken() {
        String credential = getCredential();

        MultiValueMap<String, String> formData = createTokenRequestBody();

        return webClient.post()
                .uri(authUrl)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + credential)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> jsonNode.get("access_token").asText());
    }

    private String getCredential() {
        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    private MultiValueMap<String, String> createTokenRequestBody() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        return formData;
    }

    private List<SpotifyTrackResponseDto> searchResultToTrackDto(JsonNode jsonNode) {
        JsonNode items = jsonNode.path("tracks").path("items");
        List<SpotifyTrackResponseDto> trackList = new ArrayList<>();

        for (JsonNode item : items) {
            SpotifyTrackResponseDto track = getTrackInfo(item);
            trackList.add(track);
        }

        return trackList;
    }

    private SpotifyTrackResponseDto getTrackInfo(JsonNode item) {
        String artistNames = getArtists(item);
        String albumImageUrl = getAlbumImageUrl(item);

        SpotifyTrackResponseDto track = new SpotifyTrackResponseDto(
                item.path("name").asText(),
                artistNames,
                item.path("album").path("name").asText(),
                albumImageUrl,
                item.path("preview_url").asText(null),
                item.path("external_urls").path("spotify").asText()
        );
        return track;
    }

    private String getArtists(JsonNode item) {
        List<String> artistList = new ArrayList<>();
        for (JsonNode artist : item.path("artists")) {
            artistList.add(artist.path("name").asText());
        }
        return String.join(", ", artistList);
    }

    private String getAlbumImageUrl(JsonNode item) {
        JsonNode images = item.path("album").path("images");
        if (images.isArray() && !images.isEmpty()) {
            return images.get(0).path("url").asText();
        }

        return null;
    }
}
