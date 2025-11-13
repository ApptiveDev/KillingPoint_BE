package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.global.exception.DuplicateException;
import apptive.team5.global.exception.NotFoundEntityException;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import apptive.team5.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DiaryLikeServiceTest {

    @InjectMocks
    private DiaryLikeService diaryLikeService;

    @Mock
    private DiaryLikeLowService diaryLikeLowService;

    @Mock
    private UserLowService userLowService;

    @Mock
    private DiaryLowService diaryLowService;

    @Test
    @DisplayName("좋아요 추가 성공")
    void likeDiarySuccess() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Long userId = user.getId();
        Long diaryId = 1L;

        given(userLowService.getReferenceById(userId)).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);
        given(diaryLikeLowService.existsByUserAndDiary(user, diary)).willReturn(false);

        // when
        diaryLikeService.likeDiary(userId, diaryId);

        // then
        verify(userLowService).getReferenceById(userId);
        verify(diaryLowService).findDiaryById(diaryId);
        verify(diaryLikeLowService).existsByUserAndDiary(user, diary);
        verify(diaryLikeLowService).saveDiaryLike(any(DiaryLikeEntity.class));
        verifyNoMoreInteractions(userLowService, diaryLowService, diaryLikeLowService);
    }

    @Test
    @DisplayName("좋아요 추가 실패 - 중복 좋아요")
    void likeDiaryFail_Duplicate() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Long userId = user.getId();
        Long diaryId = 1L;

        given(userLowService.getReferenceById(userId)).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);
        given(diaryLikeLowService.existsByUserAndDiary(user, diary)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> diaryLikeService.likeDiary(userId, diaryId))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이미 좋아요를 누르셨습니다!");

        verify(userLowService).getReferenceById(userId);
        verify(diaryLowService).findDiaryById(diaryId);
        verify(diaryLikeLowService).existsByUserAndDiary(user, diary);
        verifyNoMoreInteractions(userLowService, diaryLowService, diaryLikeLowService);
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void unlikeDiarySuccess() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Long userId = user.getId();
        Long diaryId = 1L;
        DiaryLikeEntity diaryLike = new DiaryLikeEntity(user, diary);

        given(userLowService.getReferenceById(userId)).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);
        given(diaryLikeLowService.findByUserAndDiary(user, diary)).willReturn(diaryLike);

        // when
        diaryLikeService.unlikeDiary(userId, diaryId);

        // then
        verify(userLowService).getReferenceById(userId);
        verify(diaryLowService).findDiaryById(diaryId);
        verify(diaryLikeLowService).findByUserAndDiary(user, diary);
        verify(diaryLikeLowService).deleteDiaryLike(diaryLike);
        verifyNoMoreInteractions(userLowService, diaryLowService, diaryLikeLowService);
    }

    @Test
    @DisplayName("좋아요 취소 실패 - 좋아요 누르지 않음")
    void unlikeDiaryFail_NotFound() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Long userId = user.getId();
        Long diaryId = 1L;

        given(userLowService.getReferenceById(userId)).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);
        given(diaryLikeLowService.findByUserAndDiary(user, diary))
                .willThrow(new NotFoundEntityException("좋아요를 누르지 않은 킬링파트입니다!"));

        // when & then
        assertThatThrownBy(() -> diaryLikeService.unlikeDiary(userId, diaryId))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage("좋아요를 누르지 않은 킬링파트입니다!");

        verify(userLowService).getReferenceById(userId);
        verify(diaryLowService).findDiaryById(diaryId);
        verify(diaryLikeLowService).findByUserAndDiary(user, diary);
        verifyNoMoreInteractions(userLowService, diaryLowService, diaryLikeLowService);
    }
}
