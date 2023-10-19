package shoppingmall.userservice.authorization.domain;

public interface JwtTokenExpireDurationStrategy {
    long getAccessTokenExpireDuration();
    long getRefreshTokenExpireDuration();
}
