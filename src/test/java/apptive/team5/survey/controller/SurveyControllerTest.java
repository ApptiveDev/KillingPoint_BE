package apptive.team5.survey.controller;


import apptive.team5.survey.dto.SurveyCreateRequestDto;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestSecurityContextHolderInjection;
import apptive.team5.util.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static Stream<String> invalidSurveyInputs() {
        return Stream.of(
                "    ",
                "",
                "1001자".repeat(200) + "."
        );
    }


    @Test
    @DisplayName("설문조사 작성 성공")
    void createSurveySuccess() throws Exception {

        UserEntity user = TestUtil.makeUserEntity();
        userRepository.save(user);

        SurveyCreateRequestDto surveyCreateRequestDto = new SurveyCreateRequestDto("설문조사재밌다.");

        TestSecurityContextHolderInjection.inject(user.getId(), user.getRoleType());

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyCreateRequestDto))
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("invalidSurveyInputs")
    @DisplayName("설문조사 작성 실패 - 유효하지 않은 값 입력")
    void createSurveyFail(String invalidSurveyInputs) throws Exception {

        UserEntity user = TestUtil.makeUserEntity();
        userRepository.save(user);

        SurveyCreateRequestDto surveyCreateRequestDto = new SurveyCreateRequestDto(invalidSurveyInputs);

        TestSecurityContextHolderInjection.inject(user.getId(), user.getRoleType());

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyCreateRequestDto))
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isBadRequest());
    }

}
