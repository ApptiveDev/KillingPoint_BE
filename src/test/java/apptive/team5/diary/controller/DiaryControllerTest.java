package apptive.team5.diary.controller;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.diary.service.DiaryService;
import apptive.team5.spotify.service.SpotifyService;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestUtil;
import apptive.team5.util.mockuser.WithCustomMockUser;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import apptive.team5.youtube.service.YoutubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    @MockitoBean
    private YoutubeService youtubeService;

    @MockitoBean
    private SpotifyService spotifyService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = TestUtil.makeUserEntity();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("다이어리 목록 조회")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void getMyMusicDiary() throws Exception {
        // given
        diaryRepository.save(new DiaryEntity("Test Music", "Test Artist", "image.url", "video.url", "Test content", testUser));

        // when & then
        mockMvc.perform(get("/api/diaries/my")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].musicTitle").value("Test Music"));
    }

    @Test
    @DisplayName("다이어리 생성 API")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void createDiary() throws Exception {
        // given
        DiaryRequest diaryRequest = new DiaryRequest("Test Artist", "Test Music", "image.url", "Test Content");

        given(youtubeService.searchVideo(any())).willReturn(Collections.singletonList(
                new YoutubeVideoResponse("Test Title", "PT3M5S", "video.url")
        ));

        // when & then
        mockMvc.perform(post("/api/diaries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaryRequest)))
                .andExpect(status().isCreated());
    }
}
