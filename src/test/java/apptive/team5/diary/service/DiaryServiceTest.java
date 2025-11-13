package apptive.team5.diary.service;

import apptive.team5.diary.domain.DiaryEntity;
import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponseDto;
import apptive.team5.diary.dto.DiaryUpdateRequestDto;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import apptive.team5.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    @Test
    @DisplayName("내 다이어리 목록 조회")
    void getMyDiaries() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);
        Page<DiaryEntity> diaryEntityPage = new PageImpl<>(List.of(diary));
        PageRequest pageRequest = PageRequest.of(0, 5);

        given(userLowService.getReferenceById(user.getId())).willReturn(user);
        given(diaryLowService.findDiaryByUser(user, pageRequest)).willReturn(diaryEntityPage);

        // when
        Page<DiaryResponseDto> result = diaryService.getMyDiaries(user.getId(), pageRequest);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).musicTitle()).isEqualTo("Test Music");
        verify(userLowService).getReferenceById(any(Long.class));
        verify(diaryLowService).findDiaryByUser(any(UserEntity.class), any(PageRequest.class));

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }

    @Test
    @DisplayName("다이어리 생성")
    void createDiary() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        DiaryCreateRequest diaryRequest = TestUtil.makeDiaryCreateRequest();

        given(userLowService.getReferenceById(user.getId())).willReturn(user);

        // when
        diaryService.createDiary(user.getId(), diaryRequest);

        // then
        verify(userLowService).getReferenceById(any(Long.class));
        verify(diaryLowService).saveDiary(any(DiaryEntity.class));

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }

    @Test
    @DisplayName("다이어리 수정")
    void updateDiary() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        Long diaryId = 1L;
        DiaryUpdateRequestDto updateRequest = TestUtil.makeDiaryUpdateRequest();
        DiaryEntity diary = TestUtil.makeDiaryEntity(user);

        given(userLowService.getReferenceById(user.getId())).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);


        // when
        diaryService.updateDiary(user.getId(), diaryId, updateRequest);

        // then
        verify(userLowService).getReferenceById(any(Long.class));
        verify(diaryLowService).findDiaryById(any(Long.class));
        verify(diaryLowService).updateDiary(any(DiaryEntity.class), any());

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }

    @Test
    @DisplayName("다이어리 삭제")
    void deleteDiary() {
        // given
        UserEntity user = TestUtil.makeUserEntityWithId();
        Long diaryId = 1L;
        DiaryEntity diary = TestUtil.makeDiaryEntityWithId(diaryId, user);

        given(userLowService.getReferenceById(user.getId())).willReturn(user);
        given(diaryLowService.findDiaryById(diaryId)).willReturn(diary);

        // when
        diaryService.deleteDiary(user.getId(), diaryId);

        // then
        verify(userLowService).getReferenceById(any(Long.class));
        verify(diaryLowService).findDiaryById(any(Long.class));
        verify(diaryLowService).deleteDiary(any(DiaryEntity.class));

        verifyNoMoreInteractions(userLowService, diaryLowService);
    }
}
