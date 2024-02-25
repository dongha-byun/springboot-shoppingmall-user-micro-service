package shoppingmall.userservice.user.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class UserGradeInfo {

    @Enumerated(EnumType.STRING)
    private UserGrade grade;
    private int orderCount;
    private int amount;

    public UserGradeInfo(UserGrade grade, int orderCount, int amount) {
        this.grade = grade;
        this.orderCount = orderCount;
        this.amount = amount;
    }

    public Optional<UserGrade> nextGrade() {
        return this.grade.nextGrade();
    }

    public void increaseOrderAmount(int amount) {
        this.orderCount++;
        this.amount += amount;
    }
}
