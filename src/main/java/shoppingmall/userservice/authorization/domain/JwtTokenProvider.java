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

    public String createAccessToken(Long userId, String accessIp, Date date) {
        return generateToken(userId, expireDateStrategy.getAccessTokenExpireDuration(), date);
    }

    public String createRefreshToken(Long userId, String accessIp, Date date){
        return generateToken(userId, expireDateStrategy.getRefreshTokenExpireDuration(), date);
    }

    private String generateToken(Long userId, long expireTime, Date currentDate) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + expireTime))
                .signWith(secretKey)
                .compact();
    }

    public Long getUserId(String token){
        String subject = getBodyOfToken(token).getSubject();
        return Long.valueOf(subject);
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