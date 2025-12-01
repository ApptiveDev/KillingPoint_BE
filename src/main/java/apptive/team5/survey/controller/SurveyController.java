package apptive.team5.survey.controller;

import apptive.team5.survey.dto.SurveyCreateRequestDto;
import apptive.team5.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<Void> createSurvey(@AuthenticationPrincipal Long userId,
                                             @Valid @RequestBody SurveyCreateRequestDto surveyCreateRequestDto) {

        surveyService.save(surveyCreateRequestDto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
