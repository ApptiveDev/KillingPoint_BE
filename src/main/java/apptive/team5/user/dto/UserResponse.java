package apptive.team5.user.dto;

import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;

public record UserResponse(
        Long userId,
        String username,
        String tag,
        String identifier,
        String email,
        String profileImageUrl,
        UserRoleType userRoleType,
        SocialType socialType
) {

    public UserResponse(UserEntity userEntity) {
        this(   userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getTag(),
                userEntity.getIdentifier(),
                userEntity.getEmail(),
                userEntity.getProfileImageUrl(),
                userEntity.getRoleType(),
                userEntity.getSocialType());
    }
}
