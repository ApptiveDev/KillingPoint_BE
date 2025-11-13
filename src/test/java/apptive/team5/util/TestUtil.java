package apptive.team5.util;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryUpdateRequestDto;
import apptive.team5.user.domain.SocialType;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.domain.UserRoleType;
import apptive.team5.user.util.TagGenerator;

public final class TestUtil {

    public static final String userIdentifier = "GOOGLE-1234";


    public static UserEntity makeUserEntity() {
        String tag = TagGenerator.generateTag();
        return new UserEntity(
                userIdentifier,
                "example@gmail.com",
                "exampleName",
                tag,
                UserRoleType.USER,
                SocialType.GOOGLE
        );
    }

    public static UserEntity makeDifferentUserEntity(UserEntity user) {
        return new UserEntity(user.getIdentifier() + "1", user.getEmail() + "1", user.getUsername(), user.getTag() + "1", user.getRoleType(), user.getSocialType());
    }

    public static UserEntity makeUserEntityWithId() {
        String tag = TagGenerator.generateTag();
        return new UserEntity(
                1L,
                userIdentifier,
                "example@gmail.com",
                "exampleName",
                tag,
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
                DiaryScope.PUBLIC,
                "PT2M58S",
                "PT1M1S",
                "PT1M31S",
                user
        );
    }

    public static DiaryEntity makeDiaryEntityWithScope(UserEntity user, DiaryScope scope) {
        return new DiaryEntity(
                "Test Music",
                "Test Artist",
                "image.url",
                "video.url",
                "Test content",
                scope,
                "PT2M58S",
                "PT1M1S",
                "PT1M31S",
                user
        );
    }

    public static DiaryEntity makeDiaryEntityWithId(Long diaryId, UserEntity user) {
        return new DiaryEntity(
                diaryId,
                "Test Music",
                "Test Artist",
                "image.url",
                "video.url",
                "Test content",
                DiaryScope.PUBLIC,
                "PT2M58S",
                "PT1M1S",
                "PT1M31S",
                user
        );
    }

    public static DiaryCreateRequest makeDiaryCreateRequest() {
        return new DiaryCreateRequest(
                "Test Artist",
                "Test Music",
                "image.url",
                "url",
                "Test Content",
                DiaryScope.PUBLIC,
                "PT2M58S",
                "PT1M1S",
                "PT1M31S"
        );
    }

    public static DiaryUpdateRequestDto makeDiaryUpdateRequest() {
        return new DiaryUpdateRequestDto(
                "Updated Artist",
                "Updated Music",
                "updated.image.url",
                "updated.video.url",
                "Updated Content",
                DiaryScope.PUBLIC,
                "PT2M58S",
                "PT1M1S",
                "PT1M31S"
        );
    }
}
