package shoppingmall.userservice.security.handler;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import shoppingmall.userservice.login.client.AuthServiceClient;
import shoppingmall.userservice.login.client.response.ResponseAuthInfo;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final AuthServiceClient authServiceClient;

    public LoginSuccessHandler(ObjectMapper objectMapper, AuthServiceClient authServiceClient) {
        this.objectMapper = objectMapper;
        this.authServiceClient = authServiceClient;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("Authentication Success = {}", authentication);
        ResponseAuthInfo authInfo = authServiceClient.getAuthInfo(
                authentication.getName(), request.getRemoteHost(), LocalDateTime.now()
        );
        LoginSuccessResponse loginSuccessResponse = new LoginSuccessResponse(authInfo.getAccessToken());

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), loginSuccessResponse);
    }

    record LoginSuccessResponse(String accessToken){}
}
