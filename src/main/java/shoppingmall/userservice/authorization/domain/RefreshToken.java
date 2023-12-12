package shoppingmall.userservice.authorization.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.userservice.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends BaseEntity {
    @Id
    @Column(length = 1000)
    private String refreshToken;

    @Column(length = 1000)
    private String accessToken;

    @Builder
    public RefreshToken(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
