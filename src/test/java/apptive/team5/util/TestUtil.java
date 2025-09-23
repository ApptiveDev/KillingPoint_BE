package apptive.team5.util;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;

public final class TestUtil {

    public static final String userIdentifier = "GOOGLE-1234";


    public static UserEntity makeUserEntity() {
        return new UserEntity(
                userIdentifier,
                "example@gmail.com",
                "exampleName",
                UserRoleType.USER,
                SocialType.GOOGLE
        );
    }

    public static DiaryEntity makeDiaryEntity(UserEntity user) {
        return new DiaryEntity(
                "Test Music",
                "Test Artist",
                "image.url",
                "video.url",
                "Test content",
                user
        );
    }
}
