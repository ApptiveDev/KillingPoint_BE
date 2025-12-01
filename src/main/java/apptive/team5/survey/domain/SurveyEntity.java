package apptive.team5.survey.domain;

import apptive.team5.global.entity.BaseTimeEntity;
import apptive.team5.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserEntity user;

    public SurveyEntity(String content, UserEntity user) {
        this.content = content;
        this.user = user;
    }
}
