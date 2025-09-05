package apptive.team5.oauth2.dto;

import apptive.team5.user.domain.SocialType;

import java.util.Map;

public record GoogleOAuth2Rep
        (Map<String, Object> attributes) implements OAuth2Response

{
    @Override
    public SocialType getProvider() {
        return SocialType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getUsername() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
