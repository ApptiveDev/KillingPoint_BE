package apptive.team5.diary.controller;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.diary.repository.DiaryLikeRepository;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestSecurityContextHolderInjection;
import apptive.team5.util.TestUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DiaryLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private DiaryLikeRepository diaryLikeRepository;

    private UserEntity userLiker;
    private UserEntity userOwner;
    private DiaryEntity diary;

    @BeforeEach
    void setUp() {
        userLiker = TestUtil.makeUserEntity();
        userRepository.save(userLiker);

        userOwner = TestUtil.makeDifferentUserEntity(userLiker);
        userRepository.save(userOwner);

        diary = TestUtil.makeDiaryEntity(userOwner);
        diaryRepository.save(diary);

        TestSecurityContextHolderInjection.inject(userLiker.getId(), userLiker.getRoleType());
    }

    @Test
    @DisplayName("좋아요 추가 성공")
    void likeDiarySuccess() throws Exception {
        // when & then
        mockMvc.perform(post("/api/diaries/{diaryId}/like", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isCreated());

        boolean isLiked = diaryLikeRepository.existsByUserAndDiary(userLiker, diary);
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("좋아요 추가 실패 - 존재하지 않는 다이어리")
    void likeDiaryFail_DiaryNotFound() throws Exception {
        // when & then
        String responseBody = mockMvc.perform(post("/api/diaries/{diaryId}/like", 9999L)
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        assertThat(jsonNode.path("message").asText()).isEqualTo("그런 다이어리는 없습니다.");
    }

    @Test
    @DisplayName("좋아요 추가 실패 - 중복 좋아요")
    void likeDiaryFail_DuplicateLike() throws Exception {
        // given
        diaryLikeRepository.save(new DiaryLikeEntity(userLiker, diary));

        // when & then
        String responseBody = mockMvc.perform(post("/api/diaries/{diaryId}/like", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        assertThat(jsonNode.path("message").asText()).isEqualTo("이미 좋아요를 누르셨습니다!");
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void unlikeDiarySuccess() throws Exception {
        // given
        diaryLikeRepository.save(new DiaryLikeEntity(userLiker, diary));

        // when & then
        mockMvc.perform(delete("/api/diaries/{diaryId}/like", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isNoContent());

        boolean isLiked = diaryLikeRepository.existsByUserAndDiary(userLiker, diary);
        assertThat(isLiked).isFalse();
    }

    @Test
    @DisplayName("좋아요 취소 실패 - 좋아요 누르지 않은 다이어리")
    void unlikeDiaryFail_LikeNotFound() throws Exception {
        // when & then
        String responseBody = mockMvc.perform(delete("/api/diaries/{diaryId}/like", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(responseBody);
        assertThat(jsonNode.path("message").asText()).isEqualTo("좋아요를 누르지 않은 킬링파트입니다!");
    }
}
