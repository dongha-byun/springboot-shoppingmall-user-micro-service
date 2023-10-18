package shoppingmall.userservice.login.exception;

public class TryLoginLockedUserException extends IllegalStateException{
    public TryLoginLockedUserException(String s) {
        super(s);
    }
}
