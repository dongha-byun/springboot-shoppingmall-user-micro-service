package shoppingmall.userservice.authorization.presentation;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.authorization.application.AuthService;
import shoppingmall.userservice.authorization.exception.NotFoundRefreshTokenException;
import shoppingmall.userservice.authorization.exception.RefreshTokenExpiredException;
import shoppingmall.userservice.authorization.presentation.request.AuthRequest;
import shoppingmall.userservice.authorization.presentation.response.AuthFailResponse;
import shoppingmall.userservice.authorization.presentation.response.AuthResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.createAuthInfo(
                authRequest.getUserId(), authRequest.getAccessIp(), new Date()
        );

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody AuthRequest authRequest) {
        String accessToken = authService.reCreateAuthInfo(
                authRequest.getUserId(), authRequest.getAccessIp(), new Date()
        );

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }
}
