package shoppingmall.userservice.authorization.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private static final String CLAIM_ACCESS_IP = "accessIp";

    @Value("${auth.jwt.key}")
    private String key;


    private final JwtTokenExpireDurationStrategy jwtTokenExpireDurationStrategy;

    public String createAccessToken(Long userId, String accessIp, Date date) {
        return generateToken(userId, jwtTokenExpireDurationStrategy.getAccessTokenExpireDuration(), accessIp, date);
    }

    public String createRefreshToken(Long userId, String accessIp, Date date){
        return generateToken(userId, jwtTokenExpireDurationStrategy.getRefreshTokenExpireDuration(), accessIp, date);
    }

    private String generateToken(Long userId, long expireTime, String accessIp, Date currentDate) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_ACCESS_IP, accessIp)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + expireTime))
                .signWith(secretKey())
                .compact();
    }

    public Long getUserId(String token) {
        String subject = getBodyOfToken(token).getSubject();
        return Long.parseLong(subject);
    }

    public String getAccessIp(String token) {
        return (String) getBodyOfToken(token).get(CLAIM_ACCESS_IP);
    }

    // jwt 토큰 유효성 체크
    public boolean canUse(String jwtToken){
        try {
            Claims claims = getBodyOfToken(jwtToken);
            return claims.getExpiration().after(new Date());
        }catch (Exception e){
            return false;
        }
    }

    private Claims getBodyOfToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
