package shoppingmall.userservice.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StatusCheckController {

    private final Environment environment;

    @GetMapping("/health-check")
    public String healthCheck() {
        String serverPort = environment.getProperty("server.port");
        return "It's Working in User Micro Service of Shopping mall "
                + "\n Server Port : " + serverPort;
    }
}
