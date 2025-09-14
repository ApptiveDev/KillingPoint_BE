package apptive.team5.user.controller;

import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.dto.UserResponse;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestUtil;
import apptive.team5.util.mockuser.WithCustomMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("회원 정보 조회 성공")
    @Test
    @WithCustomMockUser(identifier = TestUtil.userIdentifier)
    void getMyInfoSuccess() throws Exception {

        UserEntity user = TestUtil.makeUserEntity();
        userRepository.save(user);

        String response = mockMvc.perform(get("/api/users/my"))
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponse = objectMapper.readValue(response, UserResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(userResponse.email()).isEqualTo(user.getEmail());
            softly.assertThat(userResponse.identifier()).isEqualTo(user.getIdentifier());
            softly.assertThat(userResponse.socialType()).isEqualTo(user.getSocialType());
            softly.assertThat(userResponse.userRoleType()).isEqualTo(user.getRoleType());
            softly.assertThat(userResponse.profileImageUrl()).isEqualTo(user.getProfileImageUrl());
        });
    }

    @DisplayName("회원 정보 조회 실패 - 존재하지 않는 회원")
    @Test
    @WithCustomMockUser
    void getMyInfoFail() throws Exception {


        MockHttpServletResponse response = mockMvc.perform(get("/api/users/my"))
                .andReturn().getResponse();

        String content = response.getContentAsString();


        assertSoftly(softly -> {
            softly.assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(content.contains(ExceptionCode.NOT_FOUND_USER.getDescription()));
        });

    }


}
