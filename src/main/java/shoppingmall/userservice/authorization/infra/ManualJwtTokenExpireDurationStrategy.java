package shoppingmall.userservice.authorization.infra;

import shoppingmall.userservice.authorization.domain.JwtTokenExpireDurationStrategy;

public class ManualJwtTokenExpireDurationStrategy implements JwtTokenExpireDurationStrategy {

    @Override
    public long getAccessTokenExpireDuration() {
        return 5 * 60 * 1000L; // 5 minute
    }

    @Override
    public long getRefreshTokenExpireDuration() {
        return 14 * 24 * 60 * 60 * 1000L; // 14 days
    }
}
