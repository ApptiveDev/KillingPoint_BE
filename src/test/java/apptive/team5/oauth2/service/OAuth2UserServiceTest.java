package apptive.team5.oauth2.service;

import apptive.team5.oauth2.dto.GoogleOAuth2Rep;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.service.UserLowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2UserServiceTest {

    @InjectMocks
    private OAuth2UserService oAuth2UserService;

    @Mock
    private UserLowService userLowService;



    @Test
    @DisplayName("소셜 로그인 성공(회원가입)")
    void socialLoginSuccessCase1() {

        // given
        GoogleOAuth2Rep user = socialLoginCase();

        String identifier = user.getProvider() + "_" + user.getProviderId();

        given(userLowService.existsByIdentifier(any()))
                .willReturn(false);

        given(userLowService.save(any()))
                .willReturn(new UserEntity(identifier, user.getEmail(),
                        user.getUsername(), UserRoleType.USER, user.getProvider()));

        // when

        UserEntity userEntity = oAuth2UserService.socialLogin(user);

        // then

        assertSoftly(
                softly-> {
                    softly.assertThat(userEntity.getIdentifier()).isEqualTo(identifier);
                    softly.assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
                    softly.assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
                    softly.assertThat(userEntity.getRoleType()).isEqualTo(UserRoleType.USER);
                }
        );

        verify(userLowService).existsByIdentifier(any());
        verify(userLowService).save(any());
        verifyNoMoreInteractions(userLowService);
    }

    @Test
    @DisplayName("소셜 로그인 성공(이미 회원가입되어있던 회원)")
    void socialLoginSuccessCase2() {
        // given
        GoogleOAuth2Rep user = socialLoginCase();

        String identifier = user.getProvider() + "_" + user.getProviderId();

        given(userLowService.existsByIdentifier(any()))
                .willReturn(true);

        given(userLowService.findByIdentifier(any()))
                .willReturn(new UserEntity(identifier, user.getEmail(),
                        user.getUsername(), UserRoleType.USER, user.getProvider()));

        // when

        UserEntity userEntity = oAuth2UserService.socialLogin(user);

        // then

        assertSoftly(
                softly-> {
                    softly.assertThat(userEntity.getIdentifier()).isEqualTo(identifier);
                    softly.assertThat(userEntity.getEmail()).isEqualTo(user.getEmail());
                    softly.assertThat(userEntity.getUsername()).isEqualTo(user.getUsername());
                    softly.assertThat(userEntity.getRoleType()).isEqualTo(UserRoleType.USER);
                }
        );

        verify(userLowService).existsByIdentifier(any());
        verify(userLowService).findByIdentifier(any());
        verifyNoMoreInteractions(userLowService);
    }

    private GoogleOAuth2Rep socialLoginCase() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "1234");
        attributes.put("email", "example@gmail.com");
        attributes.put("name", "exampleName");

        return new GoogleOAuth2Rep(attributes);
    }

}