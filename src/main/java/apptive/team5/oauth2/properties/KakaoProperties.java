package apptive.team5.oauth2.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoProperties {

    @Value("${kakao.domain.kapi}")
    private String kApiUri;

    public String getkApiUri() {
        return kApiUri;
    }
}
