package shoppingmall.userservice.login.presentation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppingmall.userservice.login.application.LoginService;
import shoppingmall.userservice.login.exception.WrongPasswordException;
import shoppingmall.userservice.login.presentation.request.LoginRequest;
import shoppingmall.userservice.login.presentation.response.LoginResponse;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/my-login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request) throws WrongPasswordException {
        String accessToken = loginService.login(
                loginRequest.getEmail(), loginRequest.getPassword(), request.getRemoteHost()
        );
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }
}
