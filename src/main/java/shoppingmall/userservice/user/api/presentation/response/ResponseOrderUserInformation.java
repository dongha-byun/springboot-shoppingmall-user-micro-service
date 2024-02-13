package shoppingmall.userservice.user.api.presentation.response;

import shoppingmall.userservice.user.api.dao.dto.UserInfoDto;

public record ResponseOrderUserInformation(Long userId, String userName, String userTelNo) {

    public static ResponseOrderUserInformation of(UserInfoDto userInfoDto) {
        return new ResponseOrderUserInformation(
                userInfoDto.userId(), userInfoDto.userName(), userInfoDto.userTelNo()
        );
    }
}
