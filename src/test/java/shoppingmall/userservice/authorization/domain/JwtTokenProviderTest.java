package shoppingmall.userservice.authorization.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.authorization.configuration.TestJwtConfiguration;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserRepository;

@Import({TestJwtConfiguration.class})
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void beforeEach(){
        user = userRepository.save(User.builder()
                .userName("테스터1").email("test1@test.com").password("test1!").telNo("010-0000-0000")
                .build());
    }

    @Test
    @DisplayName("Access token 을 생성한다.")
    void create_access_token(){
        // given
        Date currentDate = new Date();

        // when
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), "127.0.0.1", currentDate);

        // then
        assertThat(accessToken).isNotNull();

        Long userId = jwtTokenProvider.getUserId(accessToken);
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Access token 은 만료시간이 지나면 token 을 사용할 수 없다.")
    void validate_expire_date() {
        // given
        Date currentDate = new Date();
        Date pastDate = new Date(currentDate.getTime() - 10000);
        String accessIp = "127.0.0.1";

        // when
        String canUseToken = jwtTokenProvider.createAccessToken(user.getId(), accessIp, currentDate);
        String canNotUseToken = jwtTokenProvider.createAccessToken(user.getId(), accessIp, pastDate);

        // then
        assertThat(canUseToken).isNotNull();
        assertThat(jwtTokenProvider.canUse(canUseToken)).isTrue();
        assertThat(jwtTokenProvider.getUserId(canUseToken)).isEqualTo(user.getId());

        assertThat(canNotUseToken).isNotNull();
        assertThat(jwtTokenProvider.canUse(canNotUseToken)).isFalse();
    }

}