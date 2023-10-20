package shoppingmall.userservice.login.client;

import java.time.LocalDateTime;
import shoppingmall.userservice.login.client.response.ResponseAuthInfo;

public interface AuthServiceClient {

    ResponseAuthInfo getAuthInfo(Long userId, String accessIp, LocalDateTime accessTime);
}
