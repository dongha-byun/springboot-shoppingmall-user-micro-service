package shoppingmall.userservice.user.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum UserGrade {
    NORMAL("브론즈", 0, 0, 1, "bronze.png"),
    REGULAR("실버", 10, 50000, 3, "silver.png"),
    VIP("골드", 50, 150000, 5, "gold.png"),
    VVIP("다이아몬드", 100, 300000, 10, "diamond.png");

    private final String gradeName;
    private final int minOrderCondition;
    private final int minAmountCondition;
    private final int discountRate;
    private final String logo;

    UserGrade(String gradeName, int minOrderCondition, int minAmountCondition, int discountRate, String logo) {
        this.gradeName = gradeName;
        this.minOrderCondition = minOrderCondition;
        this.minAmountCondition = minAmountCondition;
        this.discountRate = discountRate;
        this.logo = logo;
    }

    public Optional<UserGrade> nextGrade() {
        int currentOrdinal = this.ordinal();
        return Arrays.stream(UserGrade.values())
                .filter(
                        userGrade -> userGrade.ordinal() == (currentOrdinal + 1)
                )
                .findAny();
    }

    public List<UserGrade> overGrades() {
        int currentOrdinal = this.ordinal();
        return Arrays.stream(UserGrade.values())
                .filter(
                        userGrade -> userGrade.ordinal() >= currentOrdinal
                )
                .collect(Collectors.toList());
    }
}
