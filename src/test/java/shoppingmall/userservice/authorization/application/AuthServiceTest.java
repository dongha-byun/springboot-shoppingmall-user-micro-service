package shoppingmall.userservice.authorization.application;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.authorization.domain.RefreshToken;
import shoppingmall.userservice.authorization.domain.RefreshTokenRepository;
import shoppingmall.userservice.authorization.exception.NotFoundRefreshTokenException;
import shoppingmall.userservice.authorization.exception.RefreshTokenExpiredException;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("인증 정보를 생성한다.")
    void create_auth_info() {
        // given

        // when
        Date currentDate = new Date();
        String accessToken = authService.createAuthInfo(100L, "127.0.0.1", currentDate);

        // then
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken).orElseThrow();

        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("refresh token 으로 새로운 인증 정보를 생성한다.")
    void re_create_auth_info_by_refresh_token() {
        // given
        LocalDateTime createLocalDateTime = LocalDateTime.now().minusMinutes(10);
        Instant createInstant = createLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date createDate = Date.from(createInstant);
        String accessToken = authService.createAuthInfo(100L, "127.0.0.1", createDate);

        // when
        LocalDateTime reCreateLocalDateTime = createLocalDateTime.plusMinutes(10);
        Instant reCreateInstant = reCreateLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date reCreateDate = Date.from(reCreateInstant);
        String newAccessToken = authService.reCreateAuthInfo(accessToken, "127.0.0.1", reCreateDate);

        // then
        assertThat(newAccessToken).isNotNull();
    }

    @Test
    @DisplayName("refresh token 의 인증 시간이 지난 경우, 사용자의 재로그인을 유도한다.")
    void re_create_auth_info_fail_with_refresh_token_expired() {
        // given
        LocalDateTime createLocalDateTime = LocalDateTime.now().minusDays(14);
        Instant createInstant = createLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date createDate = Date.from(createInstant);
        String accessToken = authService.createAuthInfo(100L, "127.0.0.1", createDate);

        // when & then
        assertThatThrownBy(
                () -> authService.reCreateAuthInfo(accessToken, "127.0.0.1", new Date())
        ).isInstanceOf(RefreshTokenExpiredException.class);
    }

    @Test
    @DisplayName("refresh token 정보가 없는 경우, 사용자의 재로그인을 유도한다.")
    void re_create_auth_info_fail_with_not_found_refresh_token() {
        // given

        // when & then
        assertThatThrownBy(
                () -> authService.reCreateAuthInfo(
                        "access-token-with-no-refresh-token", "127.0.0.1", new Date()
                )
        ).isInstanceOf(NotFoundRefreshTokenException.class);
    }

    @Test
    @DisplayName("로그아웃을 하는 경우, 저장되어있는 refresh token 정보를 삭제한다.")
    void logout() {
        // 기존에 token 발급이 되어 있음
        // given
        refreshTokenRepository.save(
                new RefreshToken(
                        "refresh-token",
                        "access-token"
                )
        );

        // when
        authService.logout("access-token");

        // then
        Optional<RefreshToken> findToken = refreshTokenRepository.findByAccessToken("access-token");
        assertThat(findToken.isPresent()).isFalse();
    }
}