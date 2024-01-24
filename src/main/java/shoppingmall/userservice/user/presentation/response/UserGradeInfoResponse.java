package shoppingmall.userservice.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import shoppingmall.userservice.user.domain.UserGrade;
import shoppingmall.userservice.user.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserGradeInfoResponse {
    private Long userId;
    private String userName;
    private String signUpDate;
    private String currentUserGrade;
    private int gradeDiscountRate;
    private String nextUserGrade;
    private int remainedOrderCountForNextGrade;
    private int remainedAmountsForNextGrade;
    private String logoUrl;

    public static UserGradeInfoResponse to(UserGradeInfoDto dto) {
        String logoUrlPrefix = "/images/logo/grade/";
        UserGrade nextGrade = dto.getNextUserGrade();
        if(nextGrade == null) {
            return new UserGradeInfoResponse(
                    dto.getUserId(), dto.getUserName(),
                    DateUtils.toStringOfLocalDateTIme(dto.getSignUpDate(), "yyyy-MM-dd"),
                    dto.getCurrentUserGrade().getGradeName(),
                    dto.getCurrentUserGrade().getDiscountRate(),
                    null, 0, 0,
                    logoUrlPrefix + dto.getLogo()
            );
        }

        int remainedOrderCountForNextGrade = nextGrade.getMinOrderCondition() - dto.getOrderCount();
        int remainedAmountsForNextGrade = nextGrade.getMinAmountCondition() - dto.getAmount();
        return new UserGradeInfoResponse(
                dto.getUserId(), dto.getUserName(),
                DateUtils.toStringOfLocalDateTIme(dto.getSignUpDate(), "yyyy-MM-dd"),
                dto.getCurrentUserGrade().getGradeName(),
                dto.getCurrentUserGrade().getDiscountRate(),
                nextGrade.getGradeName(),
                remainedOrderCountForNextGrade,
                remainedAmountsForNextGrade,
                logoUrlPrefix + dto.getLogo()
        );

    }
}
