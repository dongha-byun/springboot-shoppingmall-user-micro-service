package shoppingmall.userservice.authorization.domain;

public class TestJwtTokenExpireDurationStrategy implements JwtTokenExpireDurationStrategy {
    @Override
    public long getAccessTokenExpireDuration() {
        return 1000L;
    }

    @Override
    public long getRefreshTokenExpireDuration() {
        return 1000L;
    }
}
