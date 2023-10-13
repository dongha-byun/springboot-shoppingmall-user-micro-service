package shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.user.application.dto.FindPwRequestDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindPwRequest {
    private String name;
    private String telNo;
    private String email;

    public FindPwRequestDto toDto() {
        return new FindPwRequestDto(
                this.name, this.telNo, this.email
        );
    }
}
