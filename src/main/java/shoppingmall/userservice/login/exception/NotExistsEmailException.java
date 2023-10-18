package shoppingmall.userservice.login.exception;

public class NotExistsEmailException extends RuntimeException{
    public NotExistsEmailException() {
        super();
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
