package shoppingmall.userservice.user.api.infra;

import static shoppingmall.userservice.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import shoppingmall.userservice.user.api.dao.UserQueryDAO;
import shoppingmall.userservice.user.api.dao.dto.UserInfoDto;
import shoppingmall.userservice.user.domain.UserGrade;

@Repository
public class JPAUserQueryDAO implements UserQueryDAO {

    private final JPAQueryFactory jpaQueryFactory;

    public JPAUserQueryDAO(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<UserInfoDto> getUsers(List<Long> userIds) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                UserInfoDto.class,
                                user.id, user.userName, user.telNo,
                                user.userGradeInfo.grade
                        )
                )
                .from(user)
                .where(
                        user.id.in(userIds)
                )
                .fetch();
    }

    @Override
    public List<Long> getUserIdsAboveGrade(UserGrade userGrade) {
        return jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(
                        user.userGradeInfo.grade.eq(userGrade)
                )
                .fetch();
    }

    @Override
    public UserGrade getUserGradeOf(Long userId) {
        return jpaQueryFactory
                .select(user.userGradeInfo.grade)
                .from(user)
                .where(
                        user.id.eq(userId)
                )
                .fetchOne();
    }
}
