package apptive.team5.survey.repository;

import apptive.team5.survey.domain.SurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<SurveyEntity, Long> {
}
