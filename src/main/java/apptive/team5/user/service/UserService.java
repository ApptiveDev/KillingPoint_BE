package apptive.team5.user.service;
import apptive.team5.diary.service.DiaryLowService;
import apptive.team5.file.dto.FileUploadRequest;
import apptive.team5.file.service.S3Service;
import apptive.team5.file.service.TemporalLowService;
import apptive.team5.global.exception.DuplicateException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.global.util.S3Util;
import apptive.team5.jwt.TokenType;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.service.JwtService;
import apptive.team5.oauth2.dto.OAuth2Response;
import apptive.team5.subscribe.service.SubscribeLowService;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.dto.UserResponse;
import apptive.team5.user.dto.UserStaticsResponse;
import apptive.team5.user.dto.UserTagUpdateRequest;
import apptive.team5.user.util.TagGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserLowService userLowService;
    private final JwtService jwtService;
    private final JWTUtil jwtUtil;
    private final S3Service s3Service;
    private final TemporalLowService temporalLowService;
    private final SubscribeLowService subscribeLowService;
    private final DiaryLowService diaryLowService;

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
            throw new DuplicateException(ExceptionCode.DUPLICATE_USER_TAG.getDescription());
        }

        findUser.changeTag(userTagUpdateRequest.tag());

        return new UserResponse(findUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findByTag(String tag, Pageable pageable) {
        return userLowService.findByTag(tag,pageable)
                .map(UserResponse::new);
    }

    public UserResponse changeProfileImage(FileUploadRequest fileUploadRequest, Long userId) {
        UserEntity findUser = userLowService.findById(userId);

        String oldProfileImage = findUser.getProfileImage();

        String fileName = S3Util.extractFileName(fileUploadRequest.presignedUrl());

        findUser.changeProfileImage(fileName);

        temporalLowService.deleteById(fileUploadRequest.id());

        s3Service.deleteS3File(oldProfileImage);

        return new UserResponse(findUser);
    }

    @Transactional(readOnly = true)
    public UserStaticsResponse getUserStatics(Long userId) {
        int killingPartCount = diaryLowService.countByUserId(userId);
        int fanCount = subscribeLowService.countSubscriberBySubscribedToId(userId);
        int pickCount = subscribeLowService.countSubscribedTobySubscriberId(userId);

        return new UserStaticsResponse(fanCount, pickCount, killingPartCount);
    }


}
