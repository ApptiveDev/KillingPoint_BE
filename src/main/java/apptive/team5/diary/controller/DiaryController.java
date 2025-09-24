package apptive.team5.diary.controller;

import apptive.team5.diary.dto.DiaryCreateRequest;
import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.dto.DiaryUpdateRequest;
import apptive.team5.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/my")
    public ResponseEntity<Page<DiaryResponse>> getMyMusicDiary(
            @AuthenticationPrincipal
            String identifier,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "5")
            int size
    ) {

        Page<DiaryResponse> response = diaryService.getMyDiaries(identifier, PageRequest.of(page, size));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Void> createDiary(@AuthenticationPrincipal String identifier, @Valid @RequestBody DiaryCreateRequest diaryRequest) {
        diaryService.createDiary(identifier, diaryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<Void> updateDiary(
            @AuthenticationPrincipal
            String identifier,
            @PathVariable
            Long diaryId,
            @RequestBody
            DiaryUpdateRequest updateRequest
    ) {
        diaryService.updateDiary(identifier, diaryId, updateRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@AuthenticationPrincipal String identifier, @PathVariable Long diaryId) {
        diaryService.deleteDiary(identifier, diaryId);

        return ResponseEntity.noContent().build();
    }
}
