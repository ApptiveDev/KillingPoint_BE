package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.domain.DiaryScope;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.UserDiaryResponseDto;
import apptive.team5.diary.dto.DiaryResponseDto;
import apptive.team5.diary.dto.DiaryUpdateRequestDto;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final UserLowService userLowService;
    private final DiaryLowService diaryLowService;
    private final DiaryLikeLowService diaryLikeLowService;

    @Transactional(readOnly = true)
    public Page<DiaryResponseDto> getMyDiaries(Long userId, Pageable pageable) {
        UserEntity foundUser = userLowService.getReferenceById(userId);

        return diaryLowService.findDiaryByUser(foundUser, pageable)
                .map(DiaryResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<UserDiaryResponseDto> getUserDiaries(Long targetUserId, Long currentUserId, Pageable pageable) {
        Page<DiaryEntity> diaryPage;

        if (targetUserId.equals(currentUserId)) {
            UserEntity targetUser = userLowService.getReferenceById(targetUserId);
            diaryPage = diaryLowService.findDiaryByUser(targetUser, pageable);
        }
        else {
            List<DiaryScope> visibleScopes = List.of(DiaryScope.PUBLIC, DiaryScope.KILLING_PART);
            diaryPage = diaryLowService.findDiaryByUserAndScopeIn(targetUserId, visibleScopes, pageable);
        }

        List<Long> diaryIds = diaryPage.getContent().stream()
                .map(DiaryEntity::getId)
                .toList();

        Set<Long> likedDiaryIds = diaryLikeLowService.findLikedDiaryIdsByUser(currentUserId, diaryIds);

        Map<Long, Long> likeCountsMap = diaryLikeLowService.findLikeCountsByDiaryIds(diaryIds);

        return diaryPage.map(diary ->
                UserDiaryResponseDto.from(
                        diary,
                        likedDiaryIds.contains(diary.getId()),
                        likeCountsMap.getOrDefault(diary.getId(), 0L),
                        currentUserId
                )
        );
    }

    @Transactional(readOnly = true)
    public List<DiaryResponseDto> getMyDiariesByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return diaryLowService.findByUserIdAndPeriod(userId, startDateTime, endDateTime)
                .stream()
                .map(DiaryResponseDto::from)
                .toList();
    }

    @Transactional
    public DiaryEntity createDiary(Long userId, DiaryCreateRequest diaryRequest) {
        UserEntity foundUser = userLowService.getReferenceById(userId);

        DiaryEntity diary = DiaryCreateRequest.toEntity(diaryRequest, foundUser);

        return diaryLowService.saveDiary(diary);
    }

    @Transactional
    public void updateDiary(Long userId, Long diaryId, DiaryUpdateRequestDto updateRequest) {
        UserEntity foundUser = userLowService.getReferenceById(userId);

        DiaryEntity foundDiary = diaryLowService.findDiaryById(diaryId);

        foundDiary.validateOwner(foundUser);

        diaryLowService.updateDiary(foundDiary, DiaryUpdateRequestDto.toUpdateDto(updateRequest));
    }

    @Transactional
    public void deleteDiary(Long userId, Long diaryId) {
        UserEntity foundUser = userLowService.getReferenceById(userId);

        DiaryEntity foundDiary = diaryLowService.findDiaryById(diaryId);

        foundDiary.validateOwner(foundUser);

        diaryLikeLowService.deleteByDiaryId(diaryId);
        diaryLowService.deleteDiary(foundDiary);
    }

    @Transactional
    public void deleteByUserId(Long userId) {

        List<Long> diaryIds = diaryLowService.findDiaryByUserId(userId)
                .stream()
                .map(DiaryEntity::getId)
                .toList();

        diaryLikeLowService.deleteByDiaryIds(diaryIds);

        diaryLowService.deleteByUserId(userId);
    }

}
