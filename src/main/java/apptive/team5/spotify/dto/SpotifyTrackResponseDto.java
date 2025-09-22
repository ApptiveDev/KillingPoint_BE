package apptive.team5.spotify.dto;

public record SpotifyTrackResponseDto(
        String trackName,
        String artistNames,
        String albumName,
        String albumImageUrl,
        String previewUrl,
        String spotifyUrl
) {
}
