package apptive.team5.jwt.component;

import apptive.team5.jwt.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    private static final Long accessTokenExpiresIn = 3600L * 1000; // 1시간
    private static final Long refreshTokenExpiresIn = 604800L * 1000; // 7일;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, TokenType tokenType) {
        try {
            Claims claims = getClaims(token);
            String realTokenType = claims.get("tokenType").toString();

            if (tokenType == null) return false;
            if (!realTokenType.equals(tokenType.name())) return false;

            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public String createJWT(Long userId, String role, TokenType tokenType) {

        long expiredMs;
        String type;

        if (tokenType.equals(TokenType.ACCESS_TOKEN)) {
            expiredMs = accessTokenExpiresIn;
            type = TokenType.ACCESS_TOKEN.name();
        }
        else {
            expiredMs = refreshTokenExpiresIn;
            type = TokenType.REFRESH_TOKEN.name();
        }

        return Jwts.builder()
                .claim("userId", userId)
                .claim("role",role)
                .claim("tokenType",type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String createJWT(String userId, String role, TokenType tokenType, Long expiredMs) {

        String type;

        if (tokenType.equals(TokenType.ACCESS_TOKEN)) {
            type = TokenType.ACCESS_TOKEN.name();
        }
        else {
            type = TokenType.REFRESH_TOKEN.name();
        }

        return Jwts.builder()
                .claim("userId", userId)
                .claim("role",role)
                .claim("tokenType",type)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
