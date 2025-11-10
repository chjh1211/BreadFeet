package com.example.breadfeet_BE.auth.config.auth;

import com.example.breadfeet_BE.domain.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm; // 1. [제거] 이제 사용하지 않습니다.
import io.jsonwebtoken.security.Keys; // 2. [추가] Keys 임포트
import jakarta.annotation.PostConstruct; // 3. [추가] PostConstruct 임포트
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets; // 4. [추가] StandardCharsets 임포트
import java.security.Key; // 5. [추가] java.security.Key 임포트
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final String secretKey = "2BHEpHbUYTMxLEgowaHZBiioNwMFyU7e";
    private final long validityInMilliseconds = 3600000; // 1시간

    // 6. [추가] String secretKey 대신 사용할 Key 객체
    private Key key;

    // 7. [추가] 빈이 생성된 후(DI 완료 후) secretKey를 Key 객체로 변환하는 메서드
    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // (기존) 토큰 생성 메서드
    public String createToken(String userIdentifier, Role role) {
        Claims claims = Jwts.claims().setSubject(userIdentifier);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                // 8. [수정] deprecated된 signWith 대신 새로운 signWith(Key) 사용
                .signWith(key) // SignatureAlgorithm.HS256는 Key 객체에 이미 포함됨
                .compact();
    }

    // 2. HTTP Request 헤더에서 토큰 추출 (그대로)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 3. 토큰 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            // 9. [수정] deprecated된 parser() 대신 parserBuilder() 사용
            Jwts.parserBuilder()
                    .setSigningKey(key) // 문자열 secretKey 대신 Key 객체 사용
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 4. 토큰에서 인증 정보(Authentication) 객체 생성
    public Authentication getAuthentication(String token) {
        // 10. [수정] deprecated된 parser() 대신 parserBuilder() 사용
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 문자열 secretKey 대신 Key 객체 사용
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userIdentifier = claims.getSubject();
        String roleName = (String) claims.get("role");

        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new UsernamePasswordAuthenticationToken(userIdentifier, "", authorities);
    }
}