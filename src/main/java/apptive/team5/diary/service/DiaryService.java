package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.dto.DiaryUpdateRequest;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final UserLowService userLowService;
    private final DiaryLowService diaryLowService;

    @Transactional(readOnly = true)
    public Page<DiaryResponse> getMyDiaries(String identifier, Pageable pageable) {
        UserEntity foundUser = findUserByIdentifier(identifier);

        return diaryLowService.findDiaryByUser(foundUser, pageable)
                .map(DiaryResponse::from);
    }

    @Transactional
    public void createDiary(String identifier, DiaryCreateRequest diaryRequest) {
        UserEntity foundUser = findUserByIdentifier(identifier);

        DiaryEntity diary = DiaryCreateRequest.toEntity(diaryRequest, foundUser);

        diaryLowService.saveDiary(diary);
    }

    @Transactional
    public void updateDiary(String identifier, Long diaryId, DiaryUpdateRequest updateRequest) {
        UserEntity foundUser = findUserByIdentifier(identifier);

        DiaryEntity foundDiary = diaryLowService.findDiaryById(diaryId);

        foundDiary.validateOwner(foundUser);

        diaryLowService.updateDiary(foundDiary, DiaryUpdateRequest.toUpdateDto(updateRequest));
    }

    @Transactional
    public void deleteDiary(String identifier, Long diaryId) {
        UserEntity foundUser = findUserByIdentifier(identifier);

        DiaryEntity foundDiary = diaryLowService.findDiaryById(diaryId);

        foundDiary.validateOwner(foundUser);

        diaryLowService.deleteDiary(foundDiary);
    }

    private UserEntity findUserByIdentifier(String identifier) {
        return userLowService.findByIdentifier(identifier);
    }
}
