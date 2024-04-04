package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.entity.Refresh;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultRefreshService {
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    public ResponseEntity<?> sendRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(Objects.equals(cookie.getName(),"refresh")){
                refresh = cookie.getValue();
            }
        }

        if (!jwtUtils.isExpired(refresh)) { // 토큰이 살아 있는 경우
            if(tokenService.refreshTokenExists(refresh)){  // 토큰이 레디스에 있다면
                tokenService.removeRefreshToken(refresh); // 토큰 삭제

                String username = jwtUtils.getUserName(refresh);
                String role = jwtUtils.getRole(refresh);

                String uuid = UUID.randomUUID().toString();
                String newAccess = jwtUtils.createJwt("access", username, role, uuid,900000l);
                String newRefresh = jwtUtils.createJwt("refresh", username, role, uuid,604800000l);

                tokenService.saveRefreshToken(Refresh.builder().refresh(newRefresh).uuid(uuid).build()); // 토큰 재저장

                response.addCookie(createCookie("access", "Bearer+" + newAccess, 60*15, "/"));
                response.addCookie(createCookie("refresh", newRefresh, 60*60*24*7, "/refresh"));

                return ResponseEntity.ok().build();
            }
        }
        return null;
    }

    private Cookie createCookie(String key, String value, int age, String path){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(age); //1주일 동안 유효
        cookie.setHttpOnly(true); // js 접근불가
//        cookie.setDomain("www.t3t.shop"); // domain 설정
        cookie.setDomain("localhost");
        cookie.setSecure(false); // https 설정
        cookie.setPath(path);

        return cookie;
    }
}