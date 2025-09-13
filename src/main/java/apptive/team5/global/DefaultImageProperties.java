package apptive.team5.global;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "default-image")
@Getter
@Setter
public class DefaultImageProperties {

    private String profile;

}
