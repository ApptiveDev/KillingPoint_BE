package apptive.team5.jwt.controller;


import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.util.TestSecurityContextHolderInjection;
import apptive.team5.util.TestUtil;
import apptive.team5.user.service.UserLowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
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
    void exchangeTokenSuccess() throws Exception {

        // given
        UserEntity user = TestUtil.makeUserEntity();
        UserEntity userEntity = userLowService.save(user);

        TestSecurityContextHolderInjection.inject(userEntity.getId(), userEntity.getRoleType());

        String refreshToken = jwtUtil.createJWT(userEntity.getId(), userEntity.getRoleType().name(), TokenType.REFRESH_TOKEN);

        jwtService.saveRefreshToken(userEntity.getId(), refreshToken);

        // when
        String response = mockMvc.perform(post("/api/jwt/exchange")
                        .header("X-Refresh-Token", refreshToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResponse tokenResponse = objectMapper.readValue(response, TokenResponse.class);

        // then
        assertSoftly(softly-> {
            softly.assertThat(tokenResponse.accessToken()).isNotBlank();
            softly.assertThat(tokenResponse.refreshToken()).isNotBlank();
        }
        );


    }

    @Test
    @DisplayName("토큰 교환 실패 - 리프래시 토큰이 존재하지 않음")
    void exchangeTokenFailure() throws Exception {

        // given
        TestSecurityContextHolderInjection.inject(1L, UserRoleType.USER);

        // when & then
        mockMvc.perform(post("/api/jwt/exchange")
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription()));
    }


    @Test
    @DisplayName("토큰 교환 실패 - 리프래시 토큰이 만료되었음")
    void exchangeTokenFailure3() throws Exception {

        // given
        UserEntity user = TestUtil.makeUserEntity();
        UserEntity userEntity = userLowService.save(user);

        TestSecurityContextHolderInjection.inject(userEntity.getId(), userEntity.getRoleType());

        String refreshToken = jwtUtil.createJWT(userEntity.getIdentifier(), userEntity.getRoleType().name(), TokenType.REFRESH_TOKEN, 0L);

        // when & then
        mockMvc.perform(post("/api/jwt/exchange")
                        .header("X-Refresh-Token", refreshToken)
                        .with(securityContext(SecurityContextHolder.getContext()))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(ExceptionCode.INVALID_REFRESH_TOKEN.getDescription()));
    }


}
