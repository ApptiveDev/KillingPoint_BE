package apptive.team5.diary.domain;

import apptive.team5.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String musicTitle;
    @Column(nullable = false, length = 255)
    private String artist;
    @Column(columnDefinition = "TEXT")
    private String albumImageUrl;
    @Column(columnDefinition = "TEXT")
    private String videoUrl;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_diary_user_id_ref_user_id")
    )
    private UserEntity user;

    public DiaryEntity(
            String musicTitle,
            String artist,
            String albumImageUrl,
            String videoUrl,
            String content,
            UserEntity user
    ) {
        this(
                null,
                musicTitle,
                artist,
                albumImageUrl,
                videoUrl,
                content,
                user
        );
    }

    public void update(String musicTitle, String artist, String albumImageUrl, String videoUrl, String content) {
        updateMusicTitle(musicTitle);
        updateArtist(artist);
        updateAlbumImageUrl(albumImageUrl);
        updateVideoUrl(videoUrl);
        updateContent(content);
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

    public void validateOwner(UserEntity user) {
        if (!this.user.equals(user)) {
            throw new AccessDeniedException("해당 다이어리에 대한 권한이 없습니다.");
        }
    }
}
