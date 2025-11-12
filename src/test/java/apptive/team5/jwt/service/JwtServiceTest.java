package apptive.team5.jwt.service;

import apptive.team5.global.exception.AuthenticationException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.component.RefreshTokenEncoder;
import apptive.team5.jwt.domain.RefreshToken;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.service.UserLowService;
import apptive.team5.util.TestUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtLowService jwtLowService;

    @Mock
    private UserLowService userLowService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RefreshTokenEncoder tokenEncoder;

    @Test
    @DisplayName("리프래시 토큰 저장 성공")
    void saveRefreshTokenSuccess() {

        // given
        String refreshToken = UUID.randomUUID().toString();
        UserEntity userEntity = TestUtil.makeUserEntityWithId();

        given(userLowService.findById(any()))
                .willReturn(userEntity);

        given(tokenEncoder.encode(any()))
                .willReturn(refreshToken);

        given(jwtLowService.save(any()))
                .willReturn(new RefreshToken(userEntity,refreshToken));


        // then
        RefreshToken saveRefreshToken = jwtService.saveRefreshToken(userEntity.getId(), refreshToken);

        // then
        assertSoftly(
                softly -> {
                    softly.assertThat(saveRefreshToken.getToken()).isEqualTo(refreshToken);
                    softly.assertThat(saveRefreshToken.getUser().getIdentifier()).isEqualTo(userEntity.getIdentifier());
                }
        );

        verify(userLowService).findById(any());
        verify(jwtLowService).deleteByUser(any());
        verify(tokenEncoder).encode(any());
        verify(jwtLowService).save(any());
        verifyNoMoreInteractions(userLowService,jwtLowService,tokenEncoder);
    }

    @Test
    @DisplayName("토큰 교환 성공")
    void exchangeTokenSuccess() {
        // given

        given(jwtUtil.validateToken(any(), any()))
                .willReturn(true);

        Claims claims = mock(Claims.class);
        given(jwtUtil.getClaims(any())).willReturn(claims);
        given(claims.get("userId")).willReturn(1L);
        given(claims.get("role")).willReturn("ROLE_USER");

        given(jwtUtil.getClaims(any()))
                .willReturn(claims);

        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();

        given(jwtUtil.createJWT(any(), any(), any()))
                .willReturn(accessToken, refreshToken);

        UserEntity userEntity = TestUtil.makeUserEntityWithId();

        given(jwtLowService.findByUserId(any()))
                .willReturn(new RefreshToken(userEntity, UUID.randomUUID().toString()));

        given(tokenEncoder.match(any(), any()))
                .willReturn(true);


        given(userLowService.findById(any()))
                .willReturn(userEntity);

        given(jwtLowService.save(any()))
                .willReturn(new RefreshToken(userEntity,refreshToken));


        // when
        TokenResponse tokenResponse = jwtService.exchangeToken(refreshToken);

        assertSoftly(
                softly -> {
                    softly.assertThat(tokenResponse.accessToken()).isEqualTo(accessToken);
                    softly.assertThat(tokenResponse.refreshToken()).isEqualTo(refreshToken);
                }
        );
        verify(userLowService).findById(any());
        verify(jwtLowService).findByUserId(any());
        verify(jwtLowService).deleteByUser(any());
        verify(jwtLowService).save(any());
        verify(jwtUtil).getClaims(any());
        verify(jwtUtil).validateToken(any(), any());
        verify(jwtUtil, times(2)).createJWT(any(), any(), any());
        verifyNoMoreInteractions(userLowService, jwtUtil, jwtLowService);
    }

    @Test
    @DisplayName("토큰 교환 실패 - 쿠키가 없음")
    void exchangeTokenFailure() {

        // given


        // when & then
        assertThatThrownBy(()->jwtService.exchangeToken(null))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription());
        verifyNoMoreInteractions(userLowService, jwtUtil, jwtLowService);

    }


    @Test
    @DisplayName("토큰 교환 실패 - 리프래시 토큰이 유효하지 않을 때")
    void exchangeTokenFailure3() {

        // given


        given(jwtUtil.validateToken(any(), any()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(()->jwtService.exchangeToken("refreshToken"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(ExceptionCode.INVALID_REFRESH_TOKEN.getDescription());
        verify(jwtUtil).validateToken(any(), any());
        verifyNoMoreInteractions(userLowService, jwtUtil, jwtLowService);
    }

}
