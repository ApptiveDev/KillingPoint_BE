package apptive.team5.config;

import apptive.team5.global.DefaultImageProperties;
import apptive.team5.user.domain.UserEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        DefaultImageProperties.class
})
@RequiredArgsConstructor
public class DefaultImageConfig {

    private final DefaultImageProperties defaultImageProperties;

    @PostConstruct
    public void init() {
        UserEntity.setDefaultImage(defaultImageProperties.getProfile());
    }
}
