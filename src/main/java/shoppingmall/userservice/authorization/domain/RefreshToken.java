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
    private Long userId;

    @Column(length = 1000)
    private String refreshToken;

    @Builder
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
