package apptive.team5.user.domain;

import apptive.team5.jwt.domain.RefreshToken;
import apptive.team5.oauth2.dto.OAuth2Response;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleType roleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    public UserEntity(String identifier, String email, String username, UserRoleType roleType, SocialType socialType) {
        this.identifier = identifier;
        this.email = email;
        this.username = username;
        this.roleType = roleType;
        this.socialType = socialType;
    }

    public UserEntity(OAuth2Response oAuth2Response) {
        this.identifier = oAuth2Response.getProvider() + "-" + oAuth2Response.getProviderId();
        this.email = oAuth2Response.getEmail();
        this.username = oAuth2Response.getUsername();
        this.roleType = UserRoleType.USER;
        this.socialType = oAuth2Response.getProvider();
    }
}
