package apptive.team5.survey.service;

import apptive.team5.survey.domain.SurveyEntity;
import apptive.team5.survey.dto.SurveyCreateRequestDto;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyLowService surveyLowService;
    private final UserLowService userLowService;

    public SurveyEntity save(SurveyCreateRequestDto surveyCreateRequestDto, Long userId) {

        UserEntity userEntity = userLowService.getReferenceById(userId);

        return surveyLowService.save(new SurveyEntity(surveyCreateRequestDto.content(), userEntity));
    }

}
