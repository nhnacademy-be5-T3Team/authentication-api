package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class DefaultRefreshService {
    private final JWTUtils jwtUtils;

    public DefaultRefreshService(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> createNewRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }
        if(!Objects.equals(refresh,request.getSession().getAttribute("refresh"))){
            return new ResponseEntity<>("Refresh Token is Expired", HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(refresh)) {
            return new ResponseEntity<>("Send Token", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        }

        if (!jwtUtils.getCategory(refresh).equals("refresh")) {
            return new ResponseEntity<>("Send Proper Token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtils.getUserName(refresh);
        String role = jwtUtils.getRole(refresh);

        String newAccess = jwtUtils.createJwt("access", username, role, 600000l);
        String newRefresh = jwtUtils.createJwt("refresh", username, role, 604800000l);

        request.getSession().removeAttribute("refresh");
        request.getSession().invalidate();

        response.setHeader("access", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        request.getSession().setAttribute("refresh", newRefresh);
        // 추가로 시간설정도 해 줘야함. 아직은 안함

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60 * 7);
        cookie.setHttpOnly(true);
        return cookie;
    }
}