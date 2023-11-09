package shoppingmall.userservice.user.api.presentation.request;

import java.util.List;

public record RequestUserInfo(List<Long> userIds) {
}
