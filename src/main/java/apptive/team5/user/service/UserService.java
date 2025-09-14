package apptive.team5.user.service;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.repository.RefreshTokenRepository;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.oauth2.dto.OAuth2Response;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserLowService userLowService;
    private final JwtService jwtService;
    private final JWTUtil jwtUtil;

    public TokenResponse socialLogin(OAuth2Response oAuth2Response) {
        String identifier = oAuth2Response.getProvider() + "-" +oAuth2Response.getProviderId();

        UserEntity user;
        if (userLowService.existsByIdentifier(identifier)) {
            user = userLowService.findByIdentifier(identifier);
        }
        else {
            user = userLowService.save(new UserEntity(identifier, oAuth2Response.getEmail(), oAuth2Response.getUsername(), UserRoleType.USER, oAuth2Response.getProvider()));
        }

        String accessToken = jwtUtil.createJWT(user.getIdentifier(), "ROLE_" + user.getRoleType().name(), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJWT(user.getIdentifier(), "ROLE_" + user.getRoleType().name(), TokenType.REFRESH_TOKEN);


        jwtService.saveRefreshToken(user.getIdentifier(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public UserResponse getUserInfo(String identifier) {
        UserEntity findUser = userLowService.findByIdentifier(identifier);

        return new UserResponse(findUser);
    }


}
