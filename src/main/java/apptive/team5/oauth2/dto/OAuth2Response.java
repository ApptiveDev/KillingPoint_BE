package apptive.team5.oauth2.dto;

import apptive.team5.user.domain.SocialType;

public interface OAuth2Response {
    SocialType getProvider();
    String getProviderId();
    String getUsername();
    String getEmail();
}
