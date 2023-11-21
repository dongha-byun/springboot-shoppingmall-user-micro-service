package shoppingmall.userservice.authorization.exception;

public class RefreshTokenExpiredException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "RefreshToken의 유효시간이 만료되었습니다.";

    public RefreshTokenExpiredException() {
        this(DEFAULT_MESSAGE);
    }

    public RefreshTokenExpiredException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
