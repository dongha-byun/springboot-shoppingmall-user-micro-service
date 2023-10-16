package shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserEditDto {
    private String password;
    private String telNo;
}
