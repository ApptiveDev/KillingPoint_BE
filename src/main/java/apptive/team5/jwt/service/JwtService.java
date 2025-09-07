package apptive.team5.jwt.service;

import apptive.team5.global.exception.AuthenticationException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.component.JWTUtil;
import apptive.team5.jwt.domain.RefreshToken;
import apptive.team5.jwt.dto.TokenResponse;
import apptive.team5.jwt.repository.RefreshTokenRepository;
import apptive.team5.jwt.component.RefreshTokenEncoder;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenEncoder tokenEncoder;
    private final UserLowService userLowService;

    public RefreshToken saveRefreshToken(String identifier, String refreshToken) {

        UserEntity findUser = userLowService.findByIdentifier(identifier);

        String encodedRefreshToken = tokenEncoder.encode(refreshToken);

        refreshTokenRepository.deleteByUser(findUser);

        return refreshTokenRepository.save(new RefreshToken(findUser, encodedRefreshToken));
    }

    public TokenResponse exchangeToken(String oldRefreshToken) {

        if (oldRefreshToken == null) throw new AuthenticationException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription());

        if (!jwtUtil.validateToken(oldRefreshToken, false))
            throw new AuthenticationException(ExceptionCode.INVALID_REFRESH_TOKEN.getDescription());

        Claims claims = jwtUtil.getClaims(oldRefreshToken);
        String identifier = claims.get("identifier").toString();
        String role = claims.get("role").toString();

        RefreshToken findRefreshToken = findByUserIdentifier(identifier);

        if (!tokenEncoder.match(findRefreshToken.getToken(), oldRefreshToken)) {
            throw new AuthenticationException(ExceptionCode.INVALID_REFRESH_TOKEN.getDescription());
        }

        String newAccessToken = jwtUtil.createJWT(identifier, role, true);
        String newRefreshToken = jwtUtil.createJWT(identifier, role, false);

        saveRefreshToken(identifier, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void deleteRefreshTokenByIdentifier(String identifier) {
        UserEntity findUser = userLowService.findByIdentifier(identifier);
        refreshTokenRepository.deleteByUser(findUser);
    }

    public RefreshToken findByUserIdentifier(String identifier) {
        return refreshTokenRepository.findByUserIdentifier(identifier)
                .orElseThrow(() -> new AuthenticationException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription()));
    }

    public void deleteExpiredRefreshTokens() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(8);
        refreshTokenRepository.deleteExpiredRefreshToken(cutoff);
    }
}
