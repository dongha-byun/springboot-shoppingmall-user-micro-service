package shoppingmall.userservice.authentication.email.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import shoppingmall.userservice.authentication.email.TestEmailAuthenticationConfig;
import shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;
import shoppingmall.userservice.authentication.email.domain.Email;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCode;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCodeStore;

@Import({TestEmailAuthenticationConfig.class})
@SpringBootTest
class EmailAuthenticationServiceTest {

    @Autowired
    EmailAuthenticationCodeStore store;

    @Autowired
    EmailAuthenticationService service;

    @BeforeEach
    void setUp() {
        store.clear();
    }

    @Test
    @DisplayName("1.요청 - 인증코드를 생성하고 이메일을 발송한다.")
    void create_code_and_send_mail() {
        // 이메일에 대해 인증코드를 생성하고 인증코드를 포함한 이메일을 발송한다.
        // 1. 인증코드가 잘 저장됐는지,
        // 2. 이메일 본문이 잘 만들어 졌는지 확인한다.
        // 3. 발송 자체는 확인할 수 가 없을거같으니... -> 발송 이력을 남기면 좋을듯 하다. 성공/실패 여부를 포함해서.
        // given
        String email = "test@test.com";
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 21, 15, 0, 0);

        // when
        EmailAuthenticationInfo emailAuthenticationInfo = service.createCode(new Email(email), requestTime);

        // then
        // 1. 인증코드 저장 확인
        LocalDateTime expireTime = LocalDateTime.of(2023, 8, 21, 15, 5, 0);
        assertThat(emailAuthenticationInfo).isNotNull();
        assertThat(emailAuthenticationInfo.getEmail()).isEqualTo("test@test.com");
        assertThat(emailAuthenticationInfo.getExpireTime()).isEqualTo(expireTime);
        assertThat(emailAuthenticationInfo.getMessage()).isEqualTo("인증번호가 발송되었습니다.");

        // 2. 이메일 발송 여부 확인 -> 성공/실패 저장으로
    }

    @Test
    @DisplayName("2.성공 - 입력한 인증번호가 발급된 인증번호와 같으면 이메일 인증에 성공했다고 판단한다.")
    void check_auth_code() {
        // 1. 이메일에 대해 인증번호를 발급한다.
        // 2. 인증코드 비교 결과 확인
        // given
        Email emailForCreate = new Email("authTest@test.com");
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 21, 15, 0, 0);
        service.createCode(emailForCreate, requestTime);

        // when
        Email emailForCheck = new Email("authTest@test.com");
        EmailAuthenticationCode checkCode = new EmailAuthenticationCode("012345");
        LocalDateTime checkRequestTime = LocalDateTime.of(2023, 8, 21, 15, 1, 0);
        EmailAuthenticationInfo emailAuthenticationInfo =
                service.checkCode(emailForCheck, checkCode, checkRequestTime);


        // then
        assertThat(emailAuthenticationInfo).isNotNull();
        assertThat(emailAuthenticationInfo.getEmail()).isEqualTo("authTest@test.com");
        assertThat(emailAuthenticationInfo.getMessage()).isEqualTo("인증이 완료되었습니다.");
    }

    @Test
    @DisplayName("3.실패 - 틀린 인증번호를 입력하면, 틀렸다고 알려준다. 재발급은 하지 않는다.")
    void wrong_auth_code() {
        // 1. 인증번호 저장
        // 2. 틀린 인증번호로 체크하도록 함
        // 3. return 이 있어야 될 거 같지?
        // 4. return 을 안할거면 틀린 경우 예외를 터트리면 될거같음. -> 예외를 터트려서 controller 에서 잡아먹어야겠다.
        // given
        Email email = new Email("test@test.com");
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 15, 23, 0 ,0);
        EmailAuthenticationCode authCode = new EmailAuthenticationCode("093444", requestTime);
        store.save(email, authCode);

        // when & then
        EmailAuthenticationCode checkCode = new EmailAuthenticationCode("093440");
        assertThatIllegalArgumentException().isThrownBy(
                () -> service.checkCode(email, checkCode, requestTime.plusMinutes(1))
        ).withMessageContaining("인증번호가 맞지 않습니다.");
    }

    @Test
    @DisplayName("4.실패 - 인증번호 확인 시간이 인증번호 발급시간으로 부터 5분이 지나면, 인증번호가 재발급된다.")
    void auth_check_fail_with_expire() {
        // 1. 인증번호 발급
        // 2. 만료시간 이후 요청시간으로 인증번호 검증
        // 3. 만료 처리 및 재발급
        // 예외를 튕기기엔 후처리가 있으니
        // 예외 안튕기고 인증번호를 재발급하고 처리결과 메세지를 던져주고 메일 발송
        // given
        String mailAddress = "test@test.com";
        Email email = new Email(mailAddress);
        String authCode = "093344";
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 20, 21, 0, 0);
        EmailAuthenticationCode emailAuthCode = new EmailAuthenticationCode(authCode, requestTime);

        store.save(email, emailAuthCode);

        // when
        LocalDateTime checkRequestTime = LocalDateTime.of(2023, 8, 20, 21, 5, 1);
        EmailAuthenticationInfo emailAuthenticationInfo = service.checkCode(email, emailAuthCode, checkRequestTime);

        // then
        assertThat(emailAuthenticationInfo.getEmail()).isEqualTo("test@test.com");
        assertThat(emailAuthenticationInfo.getExpireTime()).isEqualTo(
                LocalDateTime.of(2023, 8, 20, 21, 10, 1)
        );
        assertThat(emailAuthenticationInfo.getMessage()).isEqualTo("인증번호가 재발송되었습니다.");

        EmailAuthenticationCode reCreateAuthCode = store.getCode(email);
        assertThat(reCreateAuthCode.getValue()).isEqualTo("012345");
        assertThat(reCreateAuthCode.getExpireTime()).isEqualTo(
                LocalDateTime.of(2023, 8, 20, 21, 10, 1)
        );
    }
}