package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryUpdateDto;
import apptive.team5.diary.repository.DiaryRepository;
import apptive.team5.global.exception.InvalidInputException;
import apptive.team5.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryLowService {
    private final DiaryRepository diaryRepository;

    @Transactional
    public void saveDiary(DiaryEntity diary) {
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public DiaryEntity findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new InvalidInputException("그런 다이어리는 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<DiaryEntity> findDiaryByUser(UserEntity user, Pageable pageable) {
        return diaryRepository.findByUser(user, pageable);
    }

    @Transactional
    public void updateDiary(UserEntity user, DiaryEntity diary, DiaryUpdateDto updateDto) {
        diary.validateOwner(user);

        diary.update(
                updateDto.musicTitle(),
                updateDto.artist(),
                updateDto.albumImageUrl(),
                updateDto.videoUrl(),
                updateDto.content()
        );
    }

    @Transactional
    public void deleteDiary(UserEntity user, DiaryEntity diary) {
        diary.validateOwner(user);
        diaryRepository.delete(diary);
    }

}
