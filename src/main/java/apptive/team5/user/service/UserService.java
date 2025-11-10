package apptive.team5.user.service;
import apptive.team5.global.exception.DuplicateException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.oauth2.dto.OAuth2Response;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.dto.UserResponse;
import apptive.team5.user.dto.UserTagUpdateRequest;
import apptive.team5.user.util.TagGenerator;
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
            String tag = TagGenerator.generateTag();
            user = userLowService.save(new UserEntity(identifier, oAuth2Response.getEmail(), oAuth2Response.getUsername(), tag, UserRoleType.USER, oAuth2Response.getProvider()));
        }

        String accessToken = jwtUtil.createJWT(user.getId(), "ROLE_" + user.getRoleType().name(), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtUtil.createJWT(user.getId(), "ROLE_" + user.getRoleType().name(), TokenType.REFRESH_TOKEN);


        jwtService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserInfo(Long userId) {
        UserEntity findUser = userLowService.findById(userId);

        return new UserResponse(findUser);
    }

    public void deleteUser(Long userId) {
        UserEntity userEntity = userLowService.getReferenceById(userId);
        userLowService.deleteByUser(userEntity);

    }

    public UserResponse changeTag(UserTagUpdateRequest userTagUpdateRequest, Long userId) {
        UserEntity findUser = userLowService.findById(userId);

        if (userTagUpdateRequest.tag().equals(findUser.getTag()))
            return new UserResponse(findUser);

        if (userLowService.existsByTag(userTagUpdateRequest.tag())) {
            System.out.println("hi");
            throw new DuplicateException(ExceptionCode.DUPLICATE_USER_TAG.getDescription());
        }

        findUser.changeTag(userTagUpdateRequest.tag());

        return new UserResponse(findUser);
    }


}
