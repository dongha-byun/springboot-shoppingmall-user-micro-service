package shoppingmall.userservice.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.user.application.dto.LoginUserDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginUserResponse {
    private Long userId;
    private String email;
    private String password;
    private int loginFailCount;

    public static LoginUserResponse of(LoginUserDto dto) {
        return new LoginUserResponse(
                dto.getUserId(), dto.getEmail(),
                dto.getPassword(), dto.getLoginFailCount()
        );
    }
}
