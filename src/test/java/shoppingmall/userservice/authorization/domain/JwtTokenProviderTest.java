package shoppingmall.userservice.authorization.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class JwtTokenProviderTest {

    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    JwtDecoder jwtDecoder;

    User user;

    @BeforeEach
    void beforeEach(){
        user = userRepository.save(User.builder()
                .userName("테스터1").email("test1@test.com").password("test1!").telNo("010-0000-0000")
                .build());
        jwtTokenProvider = new JwtTokenProvider(new TestJwtTokenExpireDurationStrategy(), jwtEncoder, jwtDecoder);
    }

    @Test
    @DisplayName("Access token 을 생성한다.")
    void create_access_token(){
        // given
        Date currentDate = new Date();

        // when
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), "127.0.0.1", currentDate);

        // then
        assertThat(accessToken).isNotNull();

        String email = jwtTokenProvider.getEmail(accessToken);
        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Access token 은 만료시간이 지나면 token 을 사용할 수 없다.")
    void validate_expire_date() throws InterruptedException {
        // 유효기간이 짧은 토큰을 생성하면
        // 토큰이 만료시, 리프레시 토큰에 의해 재발급받는다
        // 재발급 받은게 유효한지 체크한다.

        // given
        Date currentDate = new Date();
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), "127.0.0.1", currentDate);
        assertThat(accessToken).isNotNull();
        assertThat(jwtTokenProvider.getEmail(accessToken)).isEqualTo(user.getEmail());

        // when & then
        assertThat(jwtTokenProvider.validateExpireToken(accessToken)).isTrue();

        Thread.sleep(1000);
        assertThat(jwtTokenProvider.validateExpireToken(accessToken)).isFalse();
    }

}