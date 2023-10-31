package shoppingmall.userservice.authorization.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtTokenExpireDurationStrategy expireDateStrategy;
    private final SecretKey secretKey = Keys.hmacShaKeyFor("secret_key_of_dong_ha_do_not_snap_this".getBytes(StandardCharsets.UTF_8));

    public String createAccessToken(String email, String accessIp, Date date) {
        return generateToken(email, expireDateStrategy.getAccessTokenExpireDuration(), date);
    }

    public String createRefreshToken(String email, String accessIp, Date date){
        return generateToken(email, expireDateStrategy.getRefreshTokenExpireDuration(), date);
    }

    private String generateToken(String email, long expireTime, Date currentDate) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + expireTime))
                .signWith(secretKey)
                .compact();
    }

    public String getEmail(String token) {
        return getBodyOfToken(token).getSubject();
    }

    // jwt 토큰 유효성 체크
    public boolean validateExpireToken(String jwtToken){
        try {
            Claims claims = getBodyOfToken(jwtToken);
            return !claims.getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    private Claims getBodyOfToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
