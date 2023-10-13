package shoppingmall.userservice.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.user.application.dto.FindPwResponseDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindPwResponse {

    private Long userId;
    private String name;
    private String telNo;
    private String email;

    public static FindPwResponse of(FindPwResponseDto dto) {
        return new FindPwResponse(
                dto.getUserId(), dto.getName(), dto.getTelNo(), dto.getEmail()
        );
    }
}
