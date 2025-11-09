package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtTokenProvider {
    private final String secretKey = "breadfeet-secret-key"; // 나중에 환경변수로
    private final long validityInMilliseconds = 3600000; // 1시간

    public String createToken(String userEmail, Role role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
