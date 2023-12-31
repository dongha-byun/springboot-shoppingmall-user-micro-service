package shoppingmall.userservice.login.client;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import shoppingmall.userservice.authorization.application.AuthService;
import shoppingmall.userservice.login.client.response.ResponseAuthInfo;

@RequiredArgsConstructor
@Component
public class MemoryAuthServiceClient implements AuthServiceClient {

    private final AuthService authService;

    @Override
    public ResponseAuthInfo getAuthInfo(Long userId, String accessIp, LocalDateTime accessTime) {
        Instant instant = accessTime.atZone(ZoneId.systemDefault()).toInstant();
        Date currentDate = Date.from(instant);
        String accessToken = authService.createAuthInfo(userId, accessIp, currentDate);

        return new ResponseAuthInfo(accessToken);
    }
}
