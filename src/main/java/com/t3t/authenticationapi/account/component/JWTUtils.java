package com.t3t.authenticationapi.account.component;

import com.t3t.authenticationapi.keymanager.properties.SecretKeyProperties;
import com.t3t.authenticationapi.keymanager.service.SecretKeyManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JWT 토큰 발급 및 claim을 사용하는 Util 클래스
 * @author joohyun1996 (이주현)
 */
@Component
public class JWTUtils {
    private Key key;
    public JWTUtils(SecretKeyManagerService secretKeyManagerService, SecretKeyProperties secretKeyProperties) {
        String secret = secretKeyManagerService.getSecretValue(secretKeyProperties.getJwtSecretKeyId());
        byte[] byteSecretKEy = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKEy);
    }

    public String getUserName(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("username", String.class);
    }

    public String getRole(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
    }

    public String getCategory(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("category", String.class);
    }
    /**
     * access token과 refresh 토큰을 1대1로 맵핑해주는 임의의 UUID value
     * @return String uuid
     * @author joohyun1996 (이주현)
     */

    public String getUUID(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("uuid", String.class);
    }
    /**
     * 토큰이 만료되었는지 확인하는 메소드
     * @return true if expired. else false
     * @author joohyun1996 (이주현)
     */
    public Boolean isExpired(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public String createJwt(String category, String id, String role, String uuid, Long expiredMs){
        Claims claims = Jwts.claims();
        return Jwts.builder()
                .claim("username", id)
                .claim("role", role)
                .claim("category", category)
                .claim("uuid", uuid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * access token 자동 재발급의 경우, 토큰의 잔여 시간이 5분 이하인지 check
     * @return true / false
     * @author joohyun1996 (이주현)
     */
    public Boolean checkReIssue(String token){
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(LocalDateTime.now(), localDateTime);

        long diffSec = Math.abs(duration.toSeconds());

        return diffSec > 0 && diffSec <= 300;
    }
}
