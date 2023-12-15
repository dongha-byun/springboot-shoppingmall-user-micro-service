package shoppingmall.userservice.login.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingmall.userservice.login.exception.NotExistsEmailException;
import shoppingmall.userservice.login.exception.TryLoginLockedUserException;
import shoppingmall.userservice.login.exception.WrongPasswordException;
import shoppingmall.userservice.login.presentation.response.LoginFailResponse;

@RestControllerAdvice(assignableTypes = LoginController.class)
public class LoginControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<LoginFailResponse> notExistsEmailException(NotExistsEmailException e) {
        return ResponseEntity.badRequest().body(
                new LoginFailResponse(601, e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<LoginFailResponse> wrongPasswordException(WrongPasswordException e) {
        return ResponseEntity.badRequest().body(
                new LoginFailResponse(602, e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<LoginFailResponse> tryLoginLockedUserException(TryLoginLockedUserException e) {
        return ResponseEntity.badRequest().body(
                new LoginFailResponse(603, e.getMessage())
        );
    }
}
