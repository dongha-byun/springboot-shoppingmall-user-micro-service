package shoppingmall.userservice.authorization.presentation.request;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AuthRequest {
    private Long userId;
    private String accessIp;
    private Date currentDate;
}
