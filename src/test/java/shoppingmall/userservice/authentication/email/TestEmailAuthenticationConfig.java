package shoppingmall.userservice.authentication.email;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import shoppingmall.userservice.authentication.email.domain.AuthenticationCodeGenerator;
import shoppingmall.userservice.authentication.email.domain.EmailAuthenticationCodeStore;
import shoppingmall.userservice.authentication.email.domain.MailNotifier;
import shoppingmall.userservice.authentication.email.domain.MemoryEmailAuthenticationCodeStore;


@TestConfiguration
public class TestEmailAuthenticationConfig {

    @Bean
    public AuthenticationCodeGenerator codeGenerator() {
        return () -> "012345";
    }

    @Bean
    public EmailAuthenticationCodeStore store() {
        return new MemoryEmailAuthenticationCodeStore();
    }

    @Bean
    public MailNotifier mailNotifier() {
        return (email, title, message) -> {
            // .... 아무것도 안해도 되긴하는데....
        };
    }
}
