package apptive.team5.survey.service;

import apptive.team5.survey.domain.SurveyEntity;
import apptive.team5.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyLowService {

    private final SurveyRepository surveyRepository;

    public SurveyEntity save(SurveyEntity surveyEntity) {
        return surveyRepository.save(surveyEntity);
    }

    public void deleteByUserId(Long userId) {
        surveyRepository.deleteByUserId(userId);
    }
}
