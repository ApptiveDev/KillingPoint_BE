package apptive.team5.jwt.service;

import apptive.team5.global.exception.AuthenticationException;
import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.jwt.domain.RefreshToken;
import apptive.team5.jwt.repository.RefreshTokenRepository;
import apptive.team5.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Service
@RequiredArgsConstructor
public class JwtLowService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new AuthenticationException(ExceptionCode.NOT_EXIST_REFRESH_TOKEN.getDescription()));
    }

    public void deleteByUser(UserEntity user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteExpiredRefreshToken(LocalDateTime cutoff) {
        refreshTokenRepository.deleteExpiredRefreshToken(cutoff);
    }
}
