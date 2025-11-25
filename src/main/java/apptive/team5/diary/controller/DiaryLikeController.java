package apptive.team5.diary.controller;

import apptive.team5.diary.dto.DiaryLikeResponseDto;
import apptive.team5.diary.service.DiaryLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries/{diaryId}/like")
public class DiaryLikeController {

    private final DiaryLikeService diaryLikeService;

    @PostMapping
    public ResponseEntity<DiaryLikeResponseDto> toggleDiaryLike(
            @AuthenticationPrincipal
            Long userId,
            @PathVariable
            Long diaryId
    ) {
        DiaryLikeResponseDto responseDto = diaryLikeService.toggleDiaryLike(userId, diaryId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
