package apptive.team5.diary.controller;

import apptive.team5.diary.dto.DiaryResponse;
import apptive.team5.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries/")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/my")
    public ResponseEntity<Page<DiaryResponse>> getMyMusicDiary(
            @AuthenticationPrincipal String identifier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {


        Page<DiaryResponse> response = diaryService.getMyDiaries(identifier, PageRequest.of(page, size));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
