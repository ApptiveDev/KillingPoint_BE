package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.dto.DiaryUpdateRequest;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import apptive.team5.youtube.dto.YoutubeSearchRequest;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import apptive.team5.youtube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final UserLowService userLowService;
    private final DiaryLowService diaryLowService;
    private final YoutubeService youtubeService;

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

        DiaryEntity diary = diaryLowService.findDiaryById(diaryId);

        List<YoutubeVideoResponse> videoList = youtubeService.searchVideo(new YoutubeSearchRequest(updateRequest.artist(), updateRequest.musicTitle()));

        String videoUrl = getVideoUrl(videoList);

        diaryLowService.updateDiary(foundUser, diary, DiaryUpdateRequest.toUpdateDto(updateRequest, videoUrl));
    }

    @Transactional
    public void deleteDiary(String identifier, Long diaryId) {
        UserEntity foundUser = findUserByIdentifier(identifier);

        DiaryEntity foundDiary = diaryLowService.findDiaryById(diaryId);

        diaryLowService.deleteDiary(foundUser, foundDiary);
    }

    private UserEntity findUserByIdentifier(String identifier) {
        return userLowService.findByIdentifier(identifier);
    }

    private String getVideoUrl(List<YoutubeVideoResponse> videoList) {
        return videoList.isEmpty() ? null : videoList.getFirst().url();
    }
}
