package shoppingmall.userservice.authorization.domain;

import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtTokenExpireDurationStrategy jwtTokenExpireDurationStrategy;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public String createAccessToken(String email, String accessIp, Date date) {
        return generateToken(email, jwtTokenExpireDurationStrategy.getAccessTokenExpireDuration(), date);
    }

    public String createRefreshToken(String email, String accessIp, Date date){
        return generateToken(email, jwtTokenExpireDurationStrategy.getRefreshTokenExpireDuration(), date);
    }

    private String generateToken(String email, long expireTime, Date currentDate) {
        Instant now = currentDate.toInstant();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusMillis(expireTime))
                .subject(email)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String getEmail(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    // jwt 토큰 유효성 체크
    public boolean validateExpireToken(String jwtToken){
        try {
            Instant expiresAt = jwtDecoder.decode(jwtToken).getExpiresAt();
            return Instant.now().isBefore(expiresAt);
        }catch (Exception e){
            return false;
        }
    }
}
