package io.github.panjung99.panapi.web.util;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key = null;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Long userId) {
        String strId = Long.toString(userId);
        return Jwts.builder()
                .subject(strId)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(31, ChronoUnit.DAYS)))
                .signWith(key)
                .compact();
    }

    public String extractUserId(String jws) {
        String userId = null;
        try {
            userId = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jws)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            throw new AppException(ErrorEnum.ILLEGAL_JWT);
        }
        return userId;
    }

}
