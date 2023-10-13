package shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindPwRequestDto {
    private String name;
    private String telNo;
    private String email;
}
