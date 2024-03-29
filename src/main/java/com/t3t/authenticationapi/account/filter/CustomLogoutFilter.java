package com.t3t.authenticationapi.account.filter;

import com.t3t.authenticationapi.account.component.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtils jwtUtils;

    public CustomLogoutFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().matches("^\\/logout$")){
            filterChain.doFilter(request,response);
            return;
        }
        if(!request.getMethod().equals("POST")){
            filterChain.doFilter(request,response);
            return;
        }
        String[] accessToken = request.getHeader("access").trim().split(" ");
        String access = accessToken[1];

        if(Objects.isNull(access)){
            filterChain.doFilter(request,response);
            return;
        }
        if(jwtUtils.isExpired(access)){
            filterChain.doFilter(request,response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        Cookie refreshCookie = null;
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refresh")){
                refreshCookie = cookie;
                refresh = cookie.getValue(); // 쿠키에서 refresh값 꺼내기
            }
        }
        if(Objects.isNull(refresh)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try{
            jwtUtils.isExpired(refresh);
        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Object obj = request.getSession().getAttribute("refresh");
        if(Objects.isNull(obj)){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if(Objects.nonNull(access) && Objects.nonNull(refresh)){
            request.getSession().removeAttribute("refresh");
            request.getSession().invalidate();
        }


        Cookie cookie = refreshCookie;
        cookie.setValue(null);
        cookie.setMaxAge(0);
        cookie.setPath(" ");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
