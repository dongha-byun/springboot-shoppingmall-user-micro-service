package shoppingmall.userservice.authorization.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Test
    @DisplayName("인증 정보를 생성한다.")
    void create_auth_info() {
        // given

        // when
        Date currentDate = new Date();
        String accessToken = authService.createAuthInfo("testUser@test.com", "127.0.0.1", currentDate);

        // then
        assertThat(accessToken).isNotNull();
    }
}