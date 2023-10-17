package shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shoppingmall.userservice.user.domain.User;

@AllArgsConstructor
@Getter
public class LoginUserDto {
    private Long userId;
    private String email;
    private int loginFailCount;
    private String password;

    public static LoginUserDto of(User user) {
        return new LoginUserDto(
                user.getId(), user.getEmail(),
                user.getLoginFailCount(), user.getPassword()
        );
    }
}
