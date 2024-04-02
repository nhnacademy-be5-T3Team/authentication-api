package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.entity.Access;
import com.t3t.authenticationapi.account.entity.Refresh;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class DefaultRefreshService {
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    public ResponseEntity<?> sendRefreshToken(HttpServletRequest request, HttpServletResponse response){
        String[] strs = request.getHeader("access").trim().split(" ");
        String access = strs[1];

        if(!jwtUtils.isExpired(access)){
            if(!tokenService.accessTokenExists(access) && tokenService.blackListTokenExists(access)){
                // 만료된 토큰은 redis에 없음
                // 하지만 blacklist에는 있어야함
                String refresh = tokenService.findRefreshByBlackListToken(access);
                Cookie cookie = new Cookie("refresh", refresh);
                cookie.setMaxAge(60); // 1분만 생존 (길게 생존하면 탈취 위험)
                cookie.setHttpOnly(true);

                response.addCookie(cookie);
                return ResponseEntity.ok().build();
            }
        }
        return null;
    }

    public ResponseEntity<?> sendAccessToken(HttpServletRequest request, HttpServletResponse response){
        String[] strs = request.getHeader("access").trim().split(" ");
        String access = strs[1];

        Cookie[] cookies = request.getCookies();
        String refresh = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(!tokenService.refreshTokenExists(refresh)){
            return null;
        }

        String username = jwtUtils.getUserName(refresh);
        String role = jwtUtils.getRole(refresh);

        tokenService.removeRefreshToken(refresh);

        String newAccess = jwtUtils.createJwt("access", username, role, 600000l);
        String newRefresh = jwtUtils.createJwt("refresh", username, role, 604800000l);

        Access accessToken = Access.builder()
                .category("access")
                .userId(username)
                .access(newAccess)
                .role(role)
                .refresh(newRefresh)
                .build();

        Refresh refreshToken = Refresh.builder()
                .refresh(newRefresh)
                .role(role)
                .userId(username)
                .category("refresh").build();

        tokenService.saveAccessToken(accessToken);
        tokenService.saveRefreshToken(refreshToken);

        Cookie cookie = new Cookie("refresh", "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("");

        response.addHeader("access", "Bearer " + newAccess);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}