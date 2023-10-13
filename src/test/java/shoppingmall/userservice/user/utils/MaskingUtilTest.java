package shoppingmall.userservice.user.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MaskingUtilTest {

    @ParameterizedTest
    @CsvSource(value = {"마스크:마*크", "강남역출구:강***구"}, delimiterString = ":")
    @DisplayName("문자열 마스킹 처리")
    void masking(String input, String result){
        // given

        // when
        String masking = MaskingUtil.maskingName(input);

        // then
        assertThat(masking).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {"new:ne*", "testUser9309:te**********"}, delimiterString = ":")
    @DisplayName("이메일은 아이디 앞 2자리 제외 모두 마스킹하고, 주소는 모두 노출시킨다.")
    void email_masking(String input, String result) {
        // given
        String address = "@test.com";
        String email = input + address;

        // when
        String maskingEmail = MaskingUtil.maskingEmail(email);

        // then
        assertThat(maskingEmail).isEqualTo(result + "@test.com");
    }
}