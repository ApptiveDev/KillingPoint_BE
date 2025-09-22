package apptive.team5.spotify.service;

import apptive.team5.spotify.dto.SpotifyTrackResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    private static MultiValueMap<String, String> createTokenRequestBody() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        return formData;
    }

    private List<SpotifyTrackResponseDto> searchResultToTrackDto(JsonNode jsonNode) {
        JsonNode items = jsonNode.path("tracks").path("items");
        List<SpotifyTrackResponseDto> trackList = new ArrayList<>();

        for (JsonNode item : items) {
            String artistNames = getArtists(item);

            SpotifyTrackResponseDto track = new SpotifyTrackResponseDto(
                    item.path("name").asText(),
                    artistNames,
                    item.path("album").path("name").asText(),
                    item.path("album").path("images").get(0) != null ? item.path("album").path("imges").get(0).path("url").asText() : null,
                    item.path("preview_url").asText(null),
                    item.path("external_urls").path("spotify").asText()
            );
            trackList.add(track);
        }

        return trackList;
    }

    private static String getArtists(JsonNode item) {
        List<String> artistList = new ArrayList<>();
        for (JsonNode artist : item.path("artist")) {
            artistList.add(artist.path("name").asText());
        }
        return String.join(", ", artistList);
    }


}
