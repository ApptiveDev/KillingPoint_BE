package apptive.team5.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileImageUpdateRequest(
        @NotBlank
        String profileImage
) {
}
