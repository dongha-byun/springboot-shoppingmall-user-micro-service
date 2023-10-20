package shoppingmall.userservice.login.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.login.exception.NotExistsEmailException;
import shoppingmall.userservice.login.exception.TryLoginLockedUserException;
import shoppingmall.userservice.login.exception.WrongPasswordException;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.save(
                User.builder()
                        .userName("신규사용자")
                        .email("newUser@test.com")
                        .password("encryptPasswordBySpringSecurity")
                        .telNo("010-1234-1234")
                        .build()
        );
    }

    @Test
    @DisplayName("회원 로그인에 성공한다.")
    void login() throws WrongPasswordException {
        // given

        // when
        String accessToken = loginService.login("newUser@test.com", "encryptPasswordBySpringSecurity", "127.0.0.1");

        // then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("비밀번호가 틀리면, 로그인에 실패하고 로그인 실패 횟수가 증가한다.")
    void increase_login_fail_count_by_wrong_password() {
        // given

        // when & then
        assertThatThrownBy(
                () -> loginService.login("newUser@test.com", "wrongPassword", "127.0.0.1")
        ).isInstanceOf(WrongPasswordException.class);

        User user = userRepository.findUserByLoginInfoEmail("newUser@test.com").orElseThrow();
        assertThat(user.getLoginFailCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 이메일 주소로 로그인을 시도하면, 로그인에 실패한다.")
    void login_fail_by_not_exists_email() {
        // given

        // when & then
        assertThatThrownBy(
                () -> loginService.login("notExistsEmail@test.com", "encryptPasswordBySpringSecurity", "127.0.0.1")
        ).isInstanceOf(NotExistsEmailException.class);
    }

    @Test
    @DisplayName("로그인 실패 횟수가 초과하면 로그인할 수 없다.")
    void login_fail_by_login_fail_count_over() {
        // given
        String email = "lockedUser@test.com";
        String password = "encryptPasswordBySpringSecurity";
        LocalDateTime signUpDate = LocalDateTime.of(2023, 2, 3, 12, 0, 0);
        userRepository.save(
                new User("잠긴사용자", email, password,
                        "010-1111-2222", signUpDate, 5, true)
        );

        // when & then
        assertThatThrownBy(
                () -> loginService.login(email, password, "127.0.0.1")
        ).isInstanceOf(TryLoginLockedUserException.class);
    }
}