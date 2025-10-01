package apptive.team5.jwt.domain;

import apptive.team5.global.entity.BaseTimeEntity;
import apptive.team5.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public RefreshToken(UserEntity user, String token) {
        this.user = user;
        this.token = token;
    }

}
