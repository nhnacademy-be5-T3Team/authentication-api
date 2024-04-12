package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultRefreshService {
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        if (Objects.isNull(request.getHeader("Authority"))) {
            throw new TokenNotExistsException("Access Token Not Exists");
        }
        String access = request.getHeader("Authority").trim().split(" ")[1];
        String newAccess = null;
        String refresh = null;

        // access 만료
        try{
            // refresh 만료시 "expired"
            refresh = tokenService.findRefreshByUUID(jwtUtils.getUUID(access));
        }catch (ExpiredJwtException e){
            String expiredUUID = e.getClaims().get("uuid", String.class);
            refresh = tokenService.findRefreshByUUID(expiredUUID);
            // Refresh 토큰이 살아 있는 경우
            if (!jwtUtils.isExpired(refresh)) {
                newAccess = jwtUtils.createJwt("access", jwtUtils.getUserName(refresh),
                        jwtUtils.getRole(refresh), jwtUtils.getUUID(refresh), 900000l);
                response.addHeader("Authority", "Bearer " + newAccess);
                return ResponseEntity.ok().build();
            }
        }

        String userId = jwtUtils.getUserName(access);
        String role = jwtUtils.getRole(access);
        String uuid = jwtUtils.getUUID(access);

        // access 만료 전, refresh 만료 전
        // http 요청이 accesstoken 만료 5분 전이라 자동 로그인 처리
        if (jwtUtils.checkReIssue(access) && tokenService.refreshTokenExists(refresh)) {
            newAccess = jwtUtils.createJwt("access", userId, role, uuid, 900000l);
            response.addHeader("Authority", "Bearer " + newAccess);
            return ResponseEntity.ok().build();
        }

        // 그 외의 경우는
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}