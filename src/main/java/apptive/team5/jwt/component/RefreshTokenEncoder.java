package apptive.team5.jwt.component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class RefreshTokenEncoder {
    private static final String HMAC_ALGO = "HmacSHA256";

    private final SecretKeySpec secretKey;

    public RefreshTokenEncoder(@Value("${spring.secretBase64}") String secretBase64) {
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        this.secretKey = new SecretKeySpec(keyBytes, HMAC_ALGO);
    }

    public boolean match(String encodedToken, String plainText) {
        if (!hasText(encodedToken) || !hasText(plainText)) return false;
        byte[] expected = hmac(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] provided;
        try {
            provided = Base64.getDecoder().decode(encodedToken.trim());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return MessageDigest.isEqual(provided, expected);
    }

    public String encode(String plainText) {
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(hmac(plainTextBytes));
    }

    private byte[] hmac(byte[] data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("암호화 실패", e);
        }
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
