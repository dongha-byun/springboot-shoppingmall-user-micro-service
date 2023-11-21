package shoppingmall.userservice.authorization.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shoppingmall.userservice.authorization.exception.NotFoundRefreshTokenException;
import shoppingmall.userservice.authorization.exception.RefreshTokenExpiredException;
import shoppingmall.userservice.authorization.presentation.response.AuthFailResponse;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AuthFailResponse> notFoundRefreshTokenException(NotFoundRefreshTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new AuthFailResponse(801, e.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<AuthFailResponse> refreshTokenExpiredException(RefreshTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new AuthFailResponse(802, e.getMessage())
        );
    }
}
