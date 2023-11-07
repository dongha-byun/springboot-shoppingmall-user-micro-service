package shoppingmall.userservice.authorization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shoppingmall.userservice.authorization.domain.JwtTokenExpireDurationStrategy;
import shoppingmall.userservice.authorization.infra.ManualJwtTokenExpireDurationStrategy;

@Configuration
public class JwtConfiguration {

    @Bean
    public JwtTokenExpireDurationStrategy jwtTokenExpireDurationStrategy() {
        return new ManualJwtTokenExpireDurationStrategy();
    }
}
