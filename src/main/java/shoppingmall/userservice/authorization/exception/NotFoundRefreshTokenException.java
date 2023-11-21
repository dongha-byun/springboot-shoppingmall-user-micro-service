package shoppingmall.userservice.authorization.exception;

public class NotFoundRefreshTokenException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Refresh Token 이 존재하지 않습니다.";

    public NotFoundRefreshTokenException() {
        this(DEFAULT_MESSAGE);
    }

    public NotFoundRefreshTokenException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    public NotFoundRefreshTokenException(String message) {
        super(message);
    }

    public NotFoundRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
