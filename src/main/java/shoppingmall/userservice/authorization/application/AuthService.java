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
}
