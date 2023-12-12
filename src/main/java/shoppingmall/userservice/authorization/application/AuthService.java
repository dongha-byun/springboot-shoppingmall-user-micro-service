package shoppingmall.userservice.authorization.application;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.authorization.domain.JwtTokenProvider;
import shoppingmall.userservice.authorization.domain.RefreshToken;
import shoppingmall.userservice.authorization.domain.RefreshTokenRepository;
import shoppingmall.userservice.authorization.exception.NotFoundRefreshTokenException;
import shoppingmall.userservice.authorization.exception.RefreshTokenExpiredException;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createAuthInfo(Long userId, String accessIp, Date currentDate) {
        String accessToken = jwtTokenProvider.createAccessToken(userId, accessIp, currentDate);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, accessIp, currentDate);

        saveRefreshToken(refreshToken, accessToken);
        return accessToken;
    }

    private void saveRefreshToken(String refreshToken, String accessToken) {
        if(refreshTokenRepository.existsById(refreshToken)) {
            refreshTokenRepository.deleteById(refreshToken);
        }
        refreshTokenRepository.save(new RefreshToken(refreshToken, accessToken));
    }

    public String reCreateAuthInfo(String accessToken, String accessIp, Date currentDate) {
        RefreshToken refreshTokenEntity = findRefreshTokenBy(accessToken);
        String refreshToken = refreshTokenEntity.getRefreshToken();
        if(!jwtTokenProvider.canUse(refreshToken)) {
            throw new RefreshTokenExpiredException();
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, accessIp, currentDate);
        refreshTokenEntity.changeAccessToken(newAccessToken);

        return newAccessToken;
    }

    private RefreshToken findRefreshTokenBy(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(NotFoundRefreshTokenException::new);
    }
}
