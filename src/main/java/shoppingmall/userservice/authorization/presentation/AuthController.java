package shoppingmall.userservice.authorization.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.authorization.application.AuthService;
import shoppingmall.userservice.authorization.presentation.request.AuthRequest;
import shoppingmall.userservice.authorization.presentation.response.AuthResponse;
import shoppingmall.userservice.user.presentation.argumentresolver.AccessToken;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest,
                                             HttpServletRequest request) {
        log.info("before retrieve create auth");
        String accessToken = authService.createAuthInfo(
                authRequest.getUserId(), request.getRemoteAddr(), new Date()
        );
        log.info("after retrieve create auth");

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@AccessToken String accessToken,
                                                HttpServletRequest request) {
        String newAccessToken = authService.reCreateAuthInfo(
                accessToken, request.getRemoteAddr(), new Date()
        );

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

    @GetMapping("/test-cookie")
    public String testCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> {
                System.out.println(cookie.getName() + "=" + cookie.getValue());
            });
        }
        Cookie c1 = new Cookie("myuser_token", "abcd1234");
        response.addCookie(c1);

        return "ok";
    }
}
