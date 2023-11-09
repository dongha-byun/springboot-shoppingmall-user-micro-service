package shoppingmall.userservice.user.api.dao.dto;

import shoppingmall.userservice.user.domain.UserGrade;

public record UserInfoDto(Long userId, String userName, UserGrade grade) {
}
