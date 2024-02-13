package shoppingmall.userservice.user.api.dao;

import java.util.List;
import shoppingmall.userservice.user.api.dao.dto.UserInfoDto;
import shoppingmall.userservice.user.domain.UserGrade;

public interface UserQueryDAO {
    List<UserInfoDto> getUsers(List<Long> userIds);
    List<Long> getUserIdsAboveGrade(UserGrade userGrade);
    UserGrade getUserGradeOf(Long userId);
}
