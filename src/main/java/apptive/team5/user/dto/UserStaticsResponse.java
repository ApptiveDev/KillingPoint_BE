package apptive.team5.user.dto;

public record UserStaticsResponse(
        int fanCount, // 구독자 수
        int pickCount, // 구독대상 수
        int killingPartCount // 킬링파트 개수
) {
}
