package shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import shoppingmall.userservice.user.domain.User;

@AllArgsConstructor
@Getter
public class FindPwResponseDto {
    private Long userId;
    private String name;
    private String telNo;
    private String email;

    public static FindPwResponseDto of(User user) {
        return new FindPwResponseDto(
                user.getId(), user.getUserName(),
                user.telNo(), user.getEmail()
        );
    }
}
