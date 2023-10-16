package shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.user.application.dto.UserEditDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserEditRequest {
    private String password;
    private String telNo;

    public UserEditDto toDto() {
        return new UserEditDto(
                password, telNo
        );
    }
}
