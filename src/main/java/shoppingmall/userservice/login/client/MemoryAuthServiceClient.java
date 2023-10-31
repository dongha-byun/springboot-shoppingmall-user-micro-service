package shoppingmall.userservice.login.client;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shoppingmall.userservice.authorization.application.AuthService;
import shoppingmall.userservice.login.client.response.ResponseAuthInfo;

@RequiredArgsConstructor
@Component
public class MemoryAuthServiceClient implements AuthServiceClient {

    private final AuthService authService;

    @Override
    public ResponseAuthInfo getAuthInfo(String email, String accessIp, LocalDateTime accessTime) {
        Instant instant = accessTime.atZone(ZoneId.systemDefault()).toInstant();
        Date currentDate = Date.from(instant);
        String accessToken = authService.createAuthInfo(email, accessIp, currentDate);

        return new ResponseAuthInfo(accessToken);
    }
}
