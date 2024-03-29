package shoppingmall.userservice.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("사용자 정보를 수정한다.")
    void editUserTest() {
        // 사용자 정보에서 이름/아이디 는 변경할 수 없다.
        // given
        User user = new User("사용자1", "user1@test.com", "user1!", "010-1111-2222");

        // when
        user.updateUser("010-2222-3333", "user2@");

        // then
        assertThat(user.getUserName()).isEqualTo("사용자1");
        assertThat(user.getEmail()).isEqualTo("user1@test.com");
        assertThat(user.getPassword()).isEqualTo("user2@");
        assertThat(user.telNo()).isEqualTo("010-2222-3333");
    }

    @Test
    @DisplayName("로그인 실패횟수가 5회가 되면, 계정 잠금 처리가 된다.")
    void user_lock_test() {
        // given
        User user = User.builder()
                .userName("사용자2")
                .email("user2@test.com")
                .password("user2@")
                .telNo("010-1234-1234")
                .build();

        // when
        user.increaseLoginFailCount();
        user.increaseLoginFailCount();
        user.increaseLoginFailCount();
        user.increaseLoginFailCount();
        user.increaseLoginFailCount();

        // then
        assertThat(user.isLocked()).isTrue();
    }

    @Test
    @DisplayName("신규가입한 사용자는 일반회원등급을 가진다.")
    void confirm_current_user_grade() {
        // given
        User user = new User("테스터", "tester@test.com", "tester1!", "010-1234-1234");

        // when
        UserGrade userGrade = user.getUserGradeInfo().getGrade();

        // then
        assertThat(userGrade.getGradeName()).isEqualTo(UserGrade.NORMAL.getGradeName());
    }

    @Test
    @DisplayName("사용자의 다음 회원등급을 조회한다.")
    void get_next_user_grade() {
        // given
        User user = new User("테스터", "tester@test.com", "tester1!", "010-1234-1234");

        // when
        Optional<UserGrade> nextUserGrade = user.getNextUserGrade();

        // then
        assertThat(nextUserGrade.isPresent()).isTrue();
        assertThat(nextUserGrade.get().getGradeName()).isEqualTo(UserGrade.REGULAR.getGradeName());
    }

    @Test
    @DisplayName("사용자의 주문횟수, 주문누적액을 증가시킨다.")
    void increase_order_amount() {
        // given
        User user = new User("테스터", "tester@test.com", "tester1!", "010-1234-1234");

        // when
        user.increaseOrderAmount(19900);

        // then
        UserGradeInfo userGradeInfo = user.getUserGradeInfo();
        assertThat(userGradeInfo.getAmount()).isEqualTo(19900);
        assertThat(userGradeInfo.getOrderCount()).isEqualTo(1);
    }
}