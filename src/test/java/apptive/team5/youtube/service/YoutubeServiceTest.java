package apptive.team5.youtube.service;

import apptive.team5.youtube.YoutubeApiKeyProvider;
import apptive.team5.youtube.domain.YoutubeInfo;
import apptive.team5.youtube.dto.YoutubeSearchRequest;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(SpringExtension.class)
class YoutubeServiceTest {

    @InjectMocks
    private YoutubeService youtubeService;

    @Mock
    private YoutubeInfoLowService youtubeInfoLowService;;

    @Mock
    private YoutubeApiKeyProvider youtubeApiKeyProvider;


    @Test
    @DisplayName("이미 존재하는 YoutubeInfo면 youtube api 호출 없이 바로 반환")
    void getYoutubeVideoResponseWithSpotifyId() {

        // given
        YoutubeSearchRequest youtubeSearchRequest = new YoutubeSearchRequest("1234", "test", "test");
        YoutubeVideoResponse youtubeVideoResponse = new YoutubeVideoResponse("test", "duration", "url");

        YoutubeInfo youtubeInfo = new YoutubeInfo(youtubeSearchRequest.spotifyId(), youtubeVideoResponse);

        given(youtubeInfoLowService.findBySpotifyId(any()))
                .willReturn(Optional.of(youtubeInfo));

        // when
        List<YoutubeVideoResponse> youtubeVideoResponses = youtubeService.searchVideo(youtubeSearchRequest);

        // then
        YoutubeVideoResponse result = youtubeVideoResponses.getFirst();
        assertSoftly(softly -> {
            softly.assertThat(result.title()).isEqualTo(youtubeVideoResponse.title());
            softly.assertThat(result.duration()).isEqualTo(youtubeVideoResponse.duration());
            softly.assertThat(result.url()).isEqualTo(youtubeVideoResponse.url());
        });

        verify(youtubeInfoLowService).findBySpotifyId(any());
        verifyNoMoreInteractions(youtubeInfoLowService,  youtubeApiKeyProvider);

    }

}
