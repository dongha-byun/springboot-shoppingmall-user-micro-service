package shoppingmall.userservice.login.exception;

public class NotExistsEmailException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "존재하지 않는 이메일 입니다.";
    public NotExistsEmailException() {
        super(DEFAULT_MESSAGE);
    }

    public NotExistsEmailException(String message) {
        super(message);
    }

    public NotExistsEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistsEmailException(Throwable cause) {
        super(cause);
    }

    protected NotExistsEmailException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
