package apptive.team5.youtube.controller;

import apptive.team5.youtube.dto.YoutubeSearchRequest;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import apptive.team5.youtube.service.YoutubeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
@Validated
public class YoutubeApiController {

    private final YoutubeService youtubeService;

    @GetMapping
    public ResponseEntity<List<YoutubeVideoResponse>> searchVideo(@NotBlank  @RequestParam String id, @NotBlank @RequestParam String artist,
                                                                  @NotBlank @RequestParam String title) {

        List<YoutubeVideoResponse> response = youtubeService.searchVideo(new YoutubeSearchRequest(id, artist, title));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
