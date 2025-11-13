package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryLikeEntity;
import apptive.team5.diary.repository.DiaryLikeRepository;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.global.exception.NotFoundEntityException;
import apptive.team5.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryLikeLowService {
    private final DiaryLikeRepository diaryLikeRepository;

    public DiaryLikeEntity saveDiaryLike(DiaryLikeEntity diaryLike) {
        return diaryLikeRepository.save(diaryLike);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserAndDiary(UserEntity user, DiaryEntity diary) {
        return diaryLikeRepository.existsByUserAndDiary(user, diary);
    }

    @Transactional(readOnly = true)
    public DiaryLikeEntity findByUserAndDiary(UserEntity user, DiaryEntity diary) {
        return diaryLikeRepository.findByUserAndDiary(user, diary)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_DIARY_LIKE.getDescription()));
    }

    @Transactional(readOnly = true)
    public Set<Long> findLikedDiaryIdsByUser(Long currentUserId, List<Long> diaryIds) {
        return diaryLikeRepository.findLikedDiaryIdsByUser(currentUserId, diaryIds);
    }

    public void deleteDiaryLike(DiaryLikeEntity diaryLike) {
        diaryLikeRepository.delete(diaryLike);
    }
}
