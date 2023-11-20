package shoppingmall.userservice.authorization.application;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.authorization.domain.JwtTokenProvider;
import shoppingmall.userservice.authorization.domain.RefreshToken;
import shoppingmall.userservice.authorization.domain.RefreshTokenRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createAuthInfo(Long userId, String accessIp, Date currentDate) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, accessIp, currentDate);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, accessIp, currentDate);

        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
        return accessToken;
    }

    public String reCreateAuthInfo(Long userId, String accessIp, Date currentDate) {
        String refreshToken = findRefreshTokenBySubject(userId);
        if(!jwtTokenProvider.canUse(refreshToken)) {
            throw new IllegalStateException("RefreshToken의 유효시간이 만료되었습니다.");
        }

        return jwtTokenProvider.createAccessToken(userId, accessIp, currentDate);
    }

    private String findRefreshTokenBySubject(Long userId) {
        RefreshToken entity = refreshTokenRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("RefreshToken 이 존재하지 않습니다.")
                );
        return entity.getRefreshToken();
    }
}
