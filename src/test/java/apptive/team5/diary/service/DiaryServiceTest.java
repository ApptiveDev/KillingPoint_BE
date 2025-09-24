package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.dto.DiaryUpdateRequest;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import apptive.team5.util.TestUtil;
import apptive.team5.youtube.dto.YoutubeVideoResponse;
import apptive.team5.youtube.service.YoutubeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {
    @InjectMocks
    private DiaryService diaryService;

    @Mock
    private UserLowService userLowService;

    @Mock
    private DiaryLowService diaryLowService;

    @Mock
    private YoutubeService youtubeService;

    @Test
    @DisplayName("내 다이어리 목록 조회")
    void getMyDiaries() {
        // given
        UserEntity user = TestUtil.makeUserEntity();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Page<DiaryEntity> diaryEntityPage = new PageImpl<>(List.of(diary));
        PageRequest pageRequest = PageRequest.of(0, 5);

        given(userLowService.findByIdentifier(user.getIdentifier())).willReturn(user);
        given(diaryLowService.findDiaryByUser(user, pageRequest)).willReturn(diaryEntityPage);

        // when
        Page<DiaryResponse> result = diaryService.getMyDiaries(user.getIdentifier(), pageRequest);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).musicTitle()).isEqualTo("Test Music");
        verify(userLowService).findByIdentifier(any(String.class));
        verify(diaryLowService).findDiaryByUser(any(UserEntity.class), any(PageRequest.class));

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }

    @Test
    @DisplayName("다이어리 생성")
    void createDiary() {
        // given
        UserEntity user = TestUtil.makeUserEntity();
        DiaryCreateRequest diaryRequest = new DiaryCreateRequest("rach", "concerto", "image.url", "video.url", "test content");
        YoutubeVideoResponse youtubeVideoResponse = new YoutubeVideoResponse("Test Title", "PT3M5S", "video.url");

        given(userLowService.findByIdentifier(user.getIdentifier())).willReturn(user);

        // when
        diaryService.createDiary(user.getIdentifier(), diaryRequest);

        // then
        verify(userLowService).findByIdentifier(any(String.class));
        verify(diaryLowService).saveDiary(any(DiaryEntity.class));

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }

    @Test
    @DisplayName("다이어리 수정")
    void updateDiary() {
        // given
        UserEntity user = TestUtil.makeUserEntity();
        Long diaryId = 1L;
        DiaryUpdateRequest updateRequest = new DiaryUpdateRequest("Updated Artist", "concerto 2", "updated image url", "updated content");
        DiaryEntity diary = new DiaryEntity("Test Music", "Test Artist", "image.url", "video.url", "Test content", user);
        YoutubeVideoResponse youtubeVideoResponse = new YoutubeVideoResponse("Updated Title", "PT3M5S", "updated.video.url");

        given(userLowService.findByIdentifier(user.getIdentifier())).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);
        given(youtubeService.searchVideo(any())).willReturn(Collections.singletonList(youtubeVideoResponse));


        // when
        diaryService.updateDiary(user.getIdentifier(), diaryId, updateRequest);

        // then
        verify(userLowService).findByIdentifier(any(String.class));
        verify(diaryLowService).findDiaryById(any(Long.class));
        verify(youtubeService).searchVideo(any());
        verify(diaryLowService).updateDiary(any(UserEntity.class), any(DiaryEntity.class), any());

        verifyNoMoreInteractions(userLowService, diaryLowService, youtubeService);
    }
}
