package shoppingmall.userservice.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class LoginInfo {
    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 400, nullable = false)
    private String password;

    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    @ColumnDefault("false")
    private boolean isLock = false;

    public LoginInfo(String email, String password, int loginFailCount, boolean isLock) {
        this.email = email;
        this.password = password;
        this.loginFailCount = loginFailCount;
        this.isLock = isLock;
    }

    public boolean checkPasswordEqual(String password) {
        return this.password.equals(password);
    }

    public int increaseLoginFailCount() {
        this.loginFailCount ++;
        if(this.loginFailCount >= 5) {
            this.isLock = true;
        }

        return this.loginFailCount;
    }

    public LoginInfo changePassword(String password) {
        return new LoginInfo(this.email, password, 0, false);
    }
}
