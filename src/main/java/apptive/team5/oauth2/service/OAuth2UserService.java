package apptive.team5.oauth2.service;

import apptive.team5.oauth2.dto.CustomOAuth2User;
import apptive.team5.oauth2.dto.GoogleOAuth2Rep;
import apptive.team5.oauth2.dto.KakaoOAuth2Rep;
import apptive.team5.oauth2.dto.OAuth2Response;
import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserLowService userLowService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = parseOAuth2User(userRequest, oAuth2User);

        UserEntity user = socialLogin(oAuth2Response);

        return new CustomOAuth2User(oAuth2User.getAttributes(), user);

    }

    public UserEntity socialLogin(OAuth2Response oAuth2Response) {
        String identifier = oAuth2Response.getProvider() + "-" +oAuth2Response.getProviderId();

        if(userLowService.existsByIdentifier(identifier)) return userLowService.findByIdentifier(identifier);
        else return userLowService.save(new UserEntity(oAuth2Response));
    }


    private OAuth2Response parseOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String social = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        if (social.equalsIgnoreCase(SocialType.KAKAO.name()) || social.equalsIgnoreCase("kakao-ssr")) {
            return new KakaoOAuth2Rep(oAuth2User.getAttributes());

        }
        else if (social.equalsIgnoreCase(SocialType.GOOGLE.name())) {
            return new GoogleOAuth2Rep(oAuth2User.getAttributes());
        }
        else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜로그인 입니다.");
        }
    }
}
