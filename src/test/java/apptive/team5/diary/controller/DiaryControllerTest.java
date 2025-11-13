package apptive.team5.diary.controller;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponseDto;
import apptive.team5.diary.dto.DiaryUpdateRequestDto;
import apptive.team5.diary.dto.UserDiaryResponseDto;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.diary.service.DiaryLikeLowService;
import apptive.team5.diary.service.DiaryLowService;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestSecurityContextHolderInjection;
import apptive.team5.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private DiaryLowService diaryLowService;

    @Autowired
    private DiaryLikeLowService diaryLikeLowService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = TestUtil.makeUserEntity();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("내 다이어리 목록 조회")
    void getMyMusicDiary() throws Exception {
        // given
        DiaryEntity diary = diaryRepository.save(TestUtil.makeDiaryEntity(testUser));

        TestSecurityContextHolderInjection.inject(testUser.getId(), testUser.getRoleType());

        // when & then
        String response = mockMvc.perform(get("/api/diaries/my")
                        .param("page", "0")
                        .param("size", "5")
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        List<DiaryResponseDto> content = objectMapper.convertValue(
                jsonNode.path("content"),
                new TypeReference<List<DiaryResponseDto>>() {}
        );

        DiaryResponseDto diaryResponse = content.getFirst();

        assertSoftly(softly-> {
            softly.assertThat(content).hasSize(1);
            softly.assertThat(diaryResponse.musicTitle()).isEqualTo(diary.getMusicTitle());
            softly.assertThat(diaryResponse.artist()).isEqualTo(diary.getArtist());
            softly.assertThat(diaryResponse.duration()).isEqualTo(diary.getDuration());
            softly.assertThat(diaryResponse.start()).isEqualTo(diary.getStart());
            softly.assertThat(diaryResponse.end()).isEqualTo(diary.getEnd());
        });
    }

    @Test
    @DisplayName("타인 다이어리 조회 및 scope 확인")
    void getUserDiariesByViewer() throws Exception {
        DiaryEntity publicDiary = diaryLowService.saveDiary(TestUtil.makeDiaryEntityWithScope(testUser, DiaryScope.PUBLIC));
        DiaryEntity killingPartDiary = diaryLowService.saveDiary(TestUtil.makeDiaryEntityWithScope(testUser, DiaryScope.KILLING_PART));
        DiaryEntity privateDiary = diaryLowService.saveDiary(TestUtil.makeDiaryEntityWithScope(testUser, DiaryScope.PRIVATE));

        UserEntity viewer = userRepository.save(TestUtil.makeDifferentUserEntity(testUser));

        diaryLikeLowService.saveDiaryLike(new DiaryLikeEntity(viewer, publicDiary));

        TestSecurityContextHolderInjection.inject(viewer.getId(), viewer.getRoleType());

        // when
        String response = mockMvc.perform(get("/api/diaries/user/{userId}", testUser.getId()) // testUser의 다이어리 조회
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // then
        JsonNode jsonNode = objectMapper.readTree(response);
        List<UserDiaryResponseDto> content = objectMapper.convertValue(
                jsonNode.path("content"), new TypeReference<>() {}
        );

        Map<Long, UserDiaryResponseDto> responseMap = content.stream()
                .collect(Collectors.toMap(UserDiaryResponseDto::diaryId, Function.identity()));

        assertSoftly(softly -> {
            softly.assertThat(content).hasSize(2); // PRIVATE은 조회되면 안 됨
            softly.assertThat(responseMap.containsKey(privateDiary.getId())).isFalse();

            // PUBLIC
            UserDiaryResponseDto publicResponse = responseMap.get(publicDiary.getId());
            softly.assertThat(publicResponse.content()).isNotNull();
            softly.assertThat(publicResponse.content()).isEqualTo(publicDiary.getContent());
            softly.assertThat(publicResponse.isLiked()).isTrue();

            // KILLING_PART
            UserDiaryResponseDto killingPartResponse = responseMap.get(killingPartDiary.getId());
            softly.assertThat(killingPartResponse.content()).isEqualTo(UserDiaryResponseDto.defaultContentMsg);
            softly.assertThat(killingPartResponse.isLiked()).isFalse();
        });
    }

    @Test
    @DisplayName("내 다이어리 기간 조회 (캘린더 용)")
    void getMyDiariesByPeriod() throws Exception {
        // given
        diaryRepository.save(TestUtil.makeDiaryEntity(testUser));
        diaryRepository.save(TestUtil.makeDiaryEntity(testUser));

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());

        TestSecurityContextHolderInjection.inject(testUser.getId(), testUser.getRoleType());

        // when
        String response = mockMvc.perform(get("/api/diaries/my/calendar")
                        .param("start", startDate.toString())
                        .param("end", endDate.toString())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        List<DiaryResponseDto> content = objectMapper.readValue(response, new TypeReference<>() {});

        assertSoftly(softly -> {
            softly.assertThat(content).hasSize(2);
        });
    }

    @Test
    @DisplayName("다이어리 생성 API")
    void createDiary() throws Exception {
        // given
        DiaryCreateRequest diaryRequest = TestUtil.makeDiaryCreateRequest();

        TestSecurityContextHolderInjection.inject(testUser.getId(), testUser.getRoleType());

        // when & then
        String header = mockMvc.perform(post("/api/diaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(diaryRequest))
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(MockMvcResultMatchers.header().exists("location"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("location");

        Long id = Long.parseLong(header.substring(header.lastIndexOf("/") + 1));

        DiaryEntity diaryEntity = diaryRepository.findById(id).get();

        assertSoftly(softly-> {
            softly.assertThat(diaryEntity.getAlbumImageUrl()).isEqualTo(diaryRequest.albumImageUrl());
            softly.assertThat(diaryEntity.getEnd()).isEqualTo(diaryRequest.end());
            softly.assertThat(diaryEntity.getDuration()).isEqualTo(diaryRequest.duration());
            softly.assertThat(diaryEntity.getStart()).isEqualTo(diaryRequest.start());
            softly.assertThat(diaryEntity.getArtist()).isEqualTo(diaryRequest.artist());
            softly.assertThat(diaryEntity.getMusicTitle()).isEqualTo(diaryRequest.musicTitle());
            softly.assertThat(diaryEntity.getScope()).isEqualTo(diaryRequest.scope());
        });
    }

    @Test
    @DisplayName("다이어리 수정 API")
    void updateDiary() throws Exception {
        // given
        DiaryEntity diary = diaryRepository.save(TestUtil.makeDiaryEntity(testUser));
        DiaryUpdateRequestDto updateRequest = TestUtil.makeDiaryUpdateRequest();
        TestSecurityContextHolderInjection.inject(testUser.getId(), testUser.getRoleType());

        // when
        mockMvc.perform(put("/api/diaries/{diaryId}", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("다이어리 삭제 API")
    void deleteDiary() throws Exception {
        // given
        DiaryEntity diary = diaryRepository.save(TestUtil.makeDiaryEntity(testUser));
        TestSecurityContextHolderInjection.inject(testUser.getId(), testUser.getRoleType());

        // when & then
        mockMvc.perform(delete("/api/diaries/{diaryId}", diary.getId())
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isNoContent());
    }
}
