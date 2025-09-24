package apptive.team5.diary.controller;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.dto.DiaryUpdateRequest;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestUtil;
import apptive.team5.util.mockuser.WithCustomMockUser;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        diaryRepository.save(TestUtil.makeDiaryEntity(testUser));

        // when & then
        String response = mockMvc.perform(get("/api/diaries/my")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        List<DiaryResponse> content = objectMapper.convertValue(
                jsonNode.path("content"),
                new TypeReference<List<DiaryResponse>>() {}
        );

        assertThat(content).hasSize(1);
        assertThat(content.getFirst().musicTitle()).isEqualTo("Test Music");
        assertThat(content.getFirst().artist()).isEqualTo("Test Artist");
    }

    @Test
    @DisplayName("다이어리 생성 API")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void createDiary() throws Exception {
        // given
        DiaryCreateRequest diaryRequest = TestUtil.makeDiaryCreateRequest();

        // when & then
        mockMvc.perform(post("/api/diaries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(diaryRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("다이어리 수정 API")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void updateDiary() throws Exception {
        // given
        DiaryEntity diary = diaryRepository.save(TestUtil.makeDiaryEntity(testUser));
        DiaryUpdateRequest updateRequest = TestUtil.makeDiaryUpdateRequest();

        // when
        mockMvc.perform(put("/api/diaries/{diaryId}", diary.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("다이어리 삭제 API")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void deleteDiary() throws Exception {
        // given
        DiaryEntity diary = diaryRepository.save(TestUtil.makeDiaryEntity(testUser));

        // when & then
        mockMvc.perform(delete("/api/diaries/{diaryId}", diary.getId()))
                .andExpect(status().isNoContent());
    }
}
