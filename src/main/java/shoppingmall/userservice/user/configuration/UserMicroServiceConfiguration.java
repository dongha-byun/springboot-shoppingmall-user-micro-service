package shoppingmall.userservice.user.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shoppingmall.userservice.user.presentation.argumentresolver.GatewayAuthenticationArgumentResolver;

@RequiredArgsConstructor
@Configuration
public class UserMicroServiceConfiguration implements WebMvcConfigurer {

    private final GatewayAuthenticationArgumentResolver gatewayAuthenticationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(gatewayAuthenticationArgumentResolver);
    }
}
