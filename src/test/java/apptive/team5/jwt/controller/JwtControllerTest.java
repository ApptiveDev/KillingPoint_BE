package apptive.team5.jwt.controller;


import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.util.TestUtil;
import apptive.team5.util.mockuser.WithCustomMockUser;
import apptive.team5.user.service.UserLowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JwtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserLowService userLowService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("토큰 교환 성공")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier, role = "USER_ROLE")
    void exchangeTokenSuccess() throws Exception {

        UserEntity user = TestUtil.makeUserEntity();
        UserEntity userEntity = userLowService.save(user);

        String refreshToken = jwtUtil.createJWT(userEntity.getIdentifier(), userEntity.getRoleType().name(), TokenType.REFRESH_TOKEN);

        jwtService.saveRefreshToken(userEntity.getIdentifier(), refreshToken);

        String response = mockMvc.perform(post("/api/jwt/exchange")
                        .header("X-Refresh-Token", refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = objectMapper.readValue(response, TokenResponse.class);

        assertSoftly(softly-> {
            softly.assertThat(tokenResponse.accessToken()).isNotBlank();
            softly.assertThat(tokenResponse.refreshToken()).isNotBlank();
        }
        );


    }

    @Test
    @DisplayName("토큰 교환 실패 - 리프래시 토큰이 존재하지 않음")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier, role = "USER_ROLE")
    void exchangeTokenFailure() throws Exception {

        mockMvc.perform(post("/api/jwt/exchange"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription()));
    }


    @Test
    @DisplayName("토큰 교환 실패 - 리프래시 토큰이 만료되었음")
    @WithCustomMockUser(identifier = TestUtil.userIdentifier, role = "USER_ROLE")
    void exchangeTokenFailure3() throws Exception {

        UserEntity user = TestUtil.makeUserEntity();
        UserEntity userEntity = userLowService.save(user);

        String refreshToken = jwtUtil.createJWT(userEntity.getIdentifier(), userEntity.getRoleType().name(), TokenType.REFRESH_TOKEN, 0L);

        mockMvc.perform(post("/api/jwt/exchange")
                        .header("X-Refresh-Token", refreshToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(ExceptionCode.INVALID_REFRESH_TOKEN.getDescription()));
    }


}
