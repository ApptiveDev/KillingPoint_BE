package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.diary.repository.DiaryLikeRepository;
import apptive.team5.global.exception.DuplicateException;
import apptive.team5.global.exception.NotFoundEntityException;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryLikeService {
    private final DiaryLikeRepository diaryLikeRepository;
    private final UserLowService userLowService;
    private final DiaryLowService diaryLowService;

    public void likeDiary(Long userId, Long diaryId) {
        UserEntity user = userLowService.getReferenceById(userId);
        DiaryEntity diary = diaryLowService.getReferenceById(diaryId);

        if (diaryLikeRepository.existsByUserAndDiary(user, diary)) {
            throw new DuplicateException("이미 좋아요를 누르셨습니다!");
        }

        diaryLikeRepository.save(new DiaryLikeEntity(user, diary));
    }

    public void unlikeDiary(Long userId, Long diaryId) {
        UserEntity user = userLowService.getReferenceById(userId);
        DiaryEntity diary = diaryLowService.getReferenceById(diaryId);

        DiaryLikeEntity diaryLike = diaryLikeRepository.findByUserAndDiary(user, diary)
                .orElseThrow(() -> new NotFoundEntityException("좋아요를 누르지 않은 킬링파트입니다!"));

        diaryLikeRepository.delete(diaryLike);
    }

}
