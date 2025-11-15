package apptive.team5.user.dto;

import apptive.team5.global.util.S3Util;
import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;

public record UserSearchResponse(
        Long userId,
        String username,
        String tag,
        String identifier,
        String profileImageUrl,
        UserRoleType userRoleType,
        SocialType socialType,
        boolean isMyPick
) {

    public UserSearchResponse(UserEntity userEntity, boolean isMyPick) {
        this(   userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getTag(),
                userEntity.getIdentifier(),
                S3Util.s3Url + userEntity.getProfileImage(),
                userEntity.getRoleType(),
                userEntity.getSocialType(),
                isMyPick
        );
    }
}
