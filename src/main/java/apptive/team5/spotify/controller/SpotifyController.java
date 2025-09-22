package apptive.team5.spotify.controller;

import apptive.team5.spotify.dto.SpotifyTrackResponseDto;
import apptive.team5.spotify.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SpotifyController {
    private SpotifyService spotifyService;

    @GetMapping
    public Mono<ResponseEntity<List<SpotifyTrackResponseDto>>> searchTracks(@RequestParam("query") String query) {
        return spotifyService.searchMusic(query)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
