package shoppingmall.userservice.user.api.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserGrade;
import shoppingmall.userservice.user.domain.UserGradeInfo;
import shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class UserMicroServiceServiceTest {

    @Autowired
    UserMicroServiceService service;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("사용자의 주문횟수를 올리고, 주문 누적액을 추가한다.")
    void increase_order_amounts() {
        // given
        int beforeOrderCount = 2;
        int beforeOrderAmounts = 119000;
        User user = userRepository.save(
                new User(
                        "사용자 1", "user1@test.com", "user1!", "010-1234-1234",
                        LocalDateTime.of(2023, 11, 11, 12, 0, 0),
                        0, false,
                        new UserGradeInfo(
                                UserGrade.NORMAL, beforeOrderCount, beforeOrderAmounts
                        )
                )
        );

        // when
        int amounts = 19900;
        service.increaseOrderAmounts(user.getId(), amounts);

        // then
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        UserGradeInfo userGradeInfo = findUser.getUserGradeInfo();
        assertThat(userGradeInfo.getGrade()).isEqualTo(UserGrade.NORMAL);
        assertThat(userGradeInfo.getOrderCount()).isEqualTo(3);
        assertThat(userGradeInfo.getAmount()).isEqualTo(138900);
    }
}