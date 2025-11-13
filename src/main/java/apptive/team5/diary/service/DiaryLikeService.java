package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.global.exception.DuplicateException;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryLikeService {
    private final DiaryLikeLowService diaryLikeLowService;
    private final UserLowService userLowService;
    private final DiaryLowService diaryLowService;

    public void likeDiary(Long userId, Long diaryId) {
        UserEntity user = userLowService.getReferenceById(userId);
        DiaryEntity diary = diaryLowService.getReferenceById(diaryId);

        if (diaryLikeLowService.existsByUserAndDiary(user, diary)) {
            throw new DuplicateException("이미 좋아요를 누르셨습니다!");
        }

        diaryLikeLowService.saveDiaryLike(new DiaryLikeEntity(user, diary));
    }

    public void unlikeDiary(Long userId, Long diaryId) {
        UserEntity user = userLowService.getReferenceById(userId);
        DiaryEntity diary = diaryLowService.getReferenceById(diaryId);

        DiaryLikeEntity diaryLike = diaryLikeLowService.findByUserAndDiary(user, diary);

        diaryLikeLowService.deleteDiaryLike(diaryLike);
    }

}
