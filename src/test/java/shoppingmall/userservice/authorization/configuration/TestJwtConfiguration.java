package shoppingmall.userservice.authorization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import shoppingmall.userservice.authorization.domain.JwtTokenExpireDurationStrategy;
import shoppingmall.userservice.authorization.domain.TestJwtTokenExpireDurationStrategy;

@TestConfiguration
public class TestJwtConfiguration {

    @Bean
    public JwtTokenExpireDurationStrategy jwtTokenExpireDurationStrategy() {
        return new TestJwtTokenExpireDurationStrategy();
    }
}
