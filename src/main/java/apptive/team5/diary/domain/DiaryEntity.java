package apptive.team5.diary.domain;

import apptive.team5.global.entity.BaseTimeEntity;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "diary_entity",
        indexes = {
            @Index(name = "idx_diary_user_created", columnList = "user_id, createDateTime")
        }
)
public class DiaryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String musicTitle;
    @Column(nullable = false, length = 255)
    private String artist;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String albumImageUrl;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String videoUrl;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaryScope scope;

    @Column(nullable = false)
    private String duration;
    @Column(nullable = false)
    private String totalDuration;
    @Column(nullable = false)
    private String start;
    @Column(nullable = false)
    private String end;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private UserEntity user;

    public DiaryEntity(
            String musicTitle,
            String artist,
            String albumImageUrl,
            String videoUrl,
            String content,
            DiaryScope scope,
            String duration,
            String totalDuration,
            String start,
            String end,
            UserEntity user
    ) {
        this(
                null,
                musicTitle,
                artist,
                albumImageUrl,
                videoUrl,
                content,
                scope,
                duration,
                totalDuration,
                start,
                end,
                user
        );
    }

    public void validateOwner(UserEntity user) {
        if (!isOwner(user.getId())) {
            throw new AccessDeniedException(ExceptionCode.ACCESS_DENIED_DIARY.getDescription());
        }
    }

    public boolean isMyDiary(Long userId) {
        return isOwner(userId);
    }

    private boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

    public boolean isScopeKillingPart() {
        return this.scope == DiaryScope.KILLING_PART;
    }

    public boolean isScopePrivate() {
        return this.scope == DiaryScope.PRIVATE;
    }

    public void update(
            String musicTitle,
            String artist,
            String albumImageUrl,
            String videoUrl,
            String content,
            DiaryScope scope
    ) {
        updateMusicTitle(musicTitle);
        updateArtist(artist);
        updateAlbumImageUrl(albumImageUrl);
        updateVideoUrl(videoUrl);
        updateContent(content);
        updateScope(scope);
    }

    private void updateMusicTitle(String musicTitle) {
        if (musicTitle != null && !musicTitle.isBlank()) {
            this.musicTitle = musicTitle;
        }
    }

    private void updateArtist(String artist) {
        if (artist != null && !artist.isBlank()) {
            this.artist = artist;
        }
    }

    private void updateAlbumImageUrl(String albumImageUrl) {
        if (albumImageUrl != null && !albumImageUrl.isBlank()) {
            this.albumImageUrl = albumImageUrl;
        }
    }

    private void updateVideoUrl(String videoUrl) {
        if (videoUrl != null && !videoUrl.isBlank()) {
            this.videoUrl = videoUrl;
        }
    }

    private void updateContent(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }

    private void updateScope(DiaryScope scope) {
        if (scope != null) {
            this.scope = scope;
        }
    }
}
