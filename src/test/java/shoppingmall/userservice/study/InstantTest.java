package shoppingmall.userservice.study;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InstantTest {

    @Test
    @DisplayName("Instant class 학습 테스트 - 이전 시간인지 확인하기")
    void instant_is_before() {
        Instant now = Instant.now();
        Instant nowPlus5000 = now.plusSeconds(5000);

        boolean isBefore = now.isBefore(nowPlus5000);

        assertThat(isBefore).isTrue();
    }

    @Test
    @DisplayName("Instant class 학습 테스트 - isBefore 파라미터로 Null 들어가기")
    void instant_is_before_parameter_null() {
        Instant now = Instant.now();

        assertThatThrownBy(
                () -> now.isBefore(null)
        );
    }
}
