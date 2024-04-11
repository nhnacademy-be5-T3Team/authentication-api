package com.t3t.authenticationapi.account.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTUtils {
    private Key key;
    public JWTUtils(@Value("${spring.security.key}") String secret) {
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

    public String getUUID(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("uuid", String.class);
    }
    // 만료되었으면 true, 아니면 false
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

    public Boolean checkReIssue(String token){
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(LocalDateTime.now(), localDateTime);

        long diffSec = Math.abs(duration.toSeconds());

        return diffSec > 0 && diffSec <= 300;
    }
}
