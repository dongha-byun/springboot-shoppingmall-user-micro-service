package shoppingmall.userservice.authorization.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    String accessToken = "accessToken";
    String refreshToken = "refreshToken";

    @BeforeEach
    void beforeEach(){
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build()
        );
    }

    @Test
    @DisplayName("만료된 access token 을 통해 refresh token 을 조회한다.")
    void findByUserTest(){
        // given

        // when
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(refreshToken.getAccessToken()).isEqualTo("accessToken");
        assertThat(refreshToken.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("access token 을 통해 refresh token 을 삭제한다.")
    void deleteByUserTest(){
        // given
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow();

        // when
        refreshTokenRepository.delete(refreshTokenEntity);

        // then
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(accessToken);
        assertThat(refreshToken.isPresent()).isFalse();
    }

}