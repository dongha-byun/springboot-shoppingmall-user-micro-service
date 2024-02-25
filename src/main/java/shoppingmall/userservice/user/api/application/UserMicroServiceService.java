package shoppingmall.userservice.user.api.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.userservice.user.domain.User;
import shoppingmall.userservice.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional
@Service
public class UserMicroServiceService {
    private final UserFinder userFinder;

    public void increaseOrderAmounts(Long userId, int amount) {
        User user = userFinder.findUserById(userId);
        user.increaseOrderAmount(amount);
    }
}
