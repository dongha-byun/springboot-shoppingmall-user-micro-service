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

    @Autowired
    UserRepository userRepository;

    User saveUser;

    @BeforeEach
    void beforeEach(){
        saveUser = userRepository.save(
                User.builder()
                        .userName("테스터")
                        .email("tester@test.com")
                        .password("tester1!")
                        .telNo("010-1234-1234")
                        .build()
        );
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email("tester@test.com")
                        .refreshToken("refreshToken")
                        .build()
        );
    }

    @Test
    @DisplayName("특정 사용자의 refresh token 을 조회한다.")
    void findByUserTest(){
        // given

        // when
        RefreshToken refreshToken = refreshTokenRepository.findById(saveUser.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(refreshToken.getEmail()).isEqualTo(saveUser.getEmail());
        assertThat(refreshToken.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("특정 사용자의 refresh token 을 삭제한다.")
    void deleteByUserTest(){
        // given

        // when
        refreshTokenRepository.deleteById(saveUser.getEmail());

        // then
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(saveUser.getEmail());
        assertThat(refreshToken.isPresent()).isFalse();
    }

}