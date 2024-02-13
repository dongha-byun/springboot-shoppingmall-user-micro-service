package shoppingmall.userservice.user.api.presentation;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.user.api.dao.UserQueryDAO;
import shoppingmall.userservice.user.api.dao.dto.UserInfoDto;
import shoppingmall.userservice.user.api.presentation.request.RequestUserInfo;
import shoppingmall.userservice.user.api.presentation.response.ResponseOrderUserInformation;
import shoppingmall.userservice.user.api.presentation.response.ResponseUserInfoHasCoupon;
import shoppingmall.userservice.user.domain.UserGrade;

@RestController
public class UserMicroServiceController {

    private final UserQueryDAO userQueryDAO;

    public UserMicroServiceController(UserQueryDAO userQueryDAO) {
        this.userQueryDAO = userQueryDAO;
    }

    @PostMapping("/users/has-coupon")
    public ResponseEntity<List<ResponseUserInfoHasCoupon>> getUsersHasCoupon(@RequestBody RequestUserInfo requestUserInfo) {
        List<Long> userIds = requestUserInfo.userIds();
        List<UserInfoDto> users = userQueryDAO.getUsers(userIds);
        List<ResponseUserInfoHasCoupon> responses = users.stream()
                .map(userInfoDto -> new ResponseUserInfoHasCoupon(userInfoDto.userId(), userInfoDto.userName(),
                        userInfoDto.grade().getGradeName())).toList();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/users/above-grade")
    public ResponseEntity<List<Long>> getUserIdsAboveGrade(@RequestParam("targetGrade") String targetGrade) {
        UserGrade userGrade = UserGrade.valueOf(targetGrade);
        List<Long> userIdsAboveGrade = userQueryDAO.getUserIdsAboveGrade(userGrade);

        return ResponseEntity.ok().body(userIdsAboveGrade);
    }

    @GetMapping("/users/{userId}/discount-rate")
    public ResponseEntity<Integer> getDiscountRate(@PathVariable("userId") Long userId) {
        UserGrade userGrade = userQueryDAO.getUserGradeOf(userId);
        if(userGrade == null) {
            throw new IllegalStateException("등급 정보 조회에 실패했습니다.");
        }
        return ResponseEntity.ok(userGrade.getDiscountRate());
    }

    @PostMapping("/orders/users")
    public ResponseEntity<List<ResponseOrderUserInformation>> getUsersOfOrders(@RequestBody List<Long> userIds) {
        List<UserInfoDto> users = userQueryDAO.getUsers(userIds);
        List<ResponseOrderUserInformation> result = users.stream()
                .map(
                        ResponseOrderUserInformation::of
                ).toList();

        return ResponseEntity.ok(result);
    }

}
