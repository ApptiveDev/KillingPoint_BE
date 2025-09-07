package apptive.team5.user.service;

import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.oauth2.dto.GoogleOAuth2Rep;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.util.TestUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.*;


@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserLowService userLowService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private JwtService jwtService;

    @Test
    @DisplayName("소셜 로그인 - 존재하는 회원이면 로그인")
    void socialLoginCase1() {
        // given
        UserEntity user = TestUtil.makeUserEntity();

        given(userLowService.existsByIdentifier(any()))
                .willReturn(true);

        given(userLowService.findByIdentifier(any()))
                .willReturn(user);

        given(jwtUtil.createJWT(any(), any(), any()))
                .willReturn("accessToken", "refreshToken");

        TokenResponse tokenResponse = userService.socialLogin(socialLoginCase());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tokenResponse.accessToken()).isEqualTo("accessToken");
            softly.assertThat(tokenResponse.refreshToken()).isEqualTo("refreshToken");
        });
        verify(userLowService).existsByIdentifier(any());
        verify(userLowService).findByIdentifier(any());
        verify(jwtService).saveRefreshToken(any(),any());
        verify(jwtUtil, times(2)).createJWT(any(), any(), any());
        verifyNoMoreInteractions(userLowService, jwtUtil,jwtService);
    }

    @Test
    @DisplayName("소셜 로그인 - 존재하지 않는 회원이면 회원가입")
    void socialLoginCase2() {
        // given
        UserEntity user = TestUtil.makeUserEntity();

        given(userLowService.existsByIdentifier(any()))
                .willReturn(false);

        given(userLowService.save(any()))
                .willReturn(user);

        given(jwtUtil.createJWT(any(), any(), any()))
                .willReturn("accessToken", "refreshToken");

        TokenResponse tokenResponse = userService.socialLogin(socialLoginCase());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(tokenResponse.accessToken()).isEqualTo("accessToken");
            softly.assertThat(tokenResponse.refreshToken()).isEqualTo("refreshToken");
        });
        verify(userLowService).existsByIdentifier(any());
        verify(userLowService).save(any());
        verify(jwtService).saveRefreshToken(any(),any());
        verify(jwtUtil, times(2)).createJWT(any(), any(), any());
        verifyNoMoreInteractions(userLowService, jwtUtil, jwtService);
    }

    private GoogleOAuth2Rep socialLoginCase() {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();

        payload.setSubject(TestUtil.userIdentifier);
        payload.set("name", "이진원");
        payload.set("email", "example@naver.com");

        return new GoogleOAuth2Rep(payload);
    }

}
