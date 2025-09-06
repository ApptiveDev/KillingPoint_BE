package apptive.team5.global;

import apptive.team5.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class Scheduler {

    private final JwtService jwtService;

    @Scheduled(cron = "0 0 3 * * *")
    public void removeExpiredRefreshTokens() {
        jwtService.deleteExpiredRefreshTokens();
    }
}
