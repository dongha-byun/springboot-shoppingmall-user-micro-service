package shoppingmall.userservice.user.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import shoppingmall.userservice.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String userName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="email", column = @Column(name = "email")),
            @AttributeOverride(name="password", column = @Column(name = "password")),
            @AttributeOverride(name="loginFailCount", column = @Column(name = "login_fail_count")),
            @AttributeOverride(name="isLock", column = @Column(name = "is_lock"))
    })
    private LoginInfo loginInfo;

    private LocalDateTime signUpDate;

    @Embedded
    private TelNo telNo;

    @Embedded
    private UserGradeInfo userGradeInfo;

    public User(String userName, String email, String password, String telNo) {
        this(userName, email, password, telNo, LocalDateTime.now());
    }

    @Builder
    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate) {
        this(userName, email, password, telNo, signUpDate, 0);
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount) {
        this(userName, email, password, telNo, signUpDate, loginFailCount, false);
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount, boolean isLock) {
        this(userName, email, password, telNo, signUpDate, loginFailCount, isLock,
                new UserGradeInfo(UserGrade.NORMAL, 0, 0));
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount, boolean isLock, UserGradeInfo userGradeInfo) {
        this.userName = userName;
        this.loginInfo = new LoginInfo(email, password, loginFailCount, isLock);
        this.telNo = new TelNo(telNo);
        this.signUpDate = signUpDate;
        this.userGradeInfo = userGradeInfo;
    }


    public void updateUser(String telNo, String password) {
        this.telNo = new TelNo(telNo);
        this.loginInfo = this.loginInfo.changePassword(password);
    }

    public boolean isEqualPassword(String password) {
        return this.loginInfo.checkPasswordEqual(password);
    }

    public String telNo() {
        return this.telNo.getTelNo();
    }

    public int increaseLoginFailCount() {
        return this.loginInfo.increaseLoginFailCount();
    }

    public Optional<UserGrade> getNextUserGrade() {
        return this.userGradeInfo.nextGrade();
    }

    public boolean isLocked() {
        return this.loginInfo.isLock();
    }

    public void increaseOrderAmount(int amounts) {
        this.userGradeInfo.increaseOrderAmount(amounts);
    }

    public int discountRate() {
        return this.userGradeInfo.getGrade().getDiscountRate();
    }

    public String getPassword() {
        return this.loginInfo.getPassword();
    }

    public String getEmail() {
        return this.loginInfo.getEmail();
    }

    public int getLoginFailCount() {
        return this.loginInfo.getLoginFailCount();
    }
}
