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

        saveRefreshToken(userId, refreshToken);
        return accessToken;
    }

    private void saveRefreshToken(Long userId, String refreshToken) {
        if(refreshTokenRepository.existsById(userId)) {
            refreshTokenRepository.deleteById(userId);
        }
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken));
    }

    public String reCreateAuthInfo(Long userId, String accessIp, Date currentDate) {
        String refreshToken = findRefreshTokenBySubject(userId);
        if(!jwtTokenProvider.canUse(refreshToken)) {
            throw new RefreshTokenExpiredException();
        }

        return jwtTokenProvider.createAccessToken(userId, accessIp, currentDate);
    }

    private String findRefreshTokenBySubject(Long userId) {
        RefreshToken entity = refreshTokenRepository.findById(userId)
                .orElseThrow(NotFoundRefreshTokenException::new);
        return entity.getRefreshToken();
    }
}
