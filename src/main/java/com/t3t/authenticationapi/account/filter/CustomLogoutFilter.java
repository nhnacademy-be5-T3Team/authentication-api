package com.t3t.authenticationapi.account.filter;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getHeader("Authority").isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        String access = request.getHeader("Authority").trim().split(" ")[1];

        if (Objects.isNull(access)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (jwtUtils.isExpired(access)) {
            filterChain.doFilter(request, response);
            return;
        }

        String uuid = jwtUtils.getUUID(access);
        tokenService.saveBlackListToken(access);
        if(tokenService.refreshTokenExistsByUUID(uuid)){
            tokenService.removeRefreshTokenByUUID(uuid); // redis에 있는 RTK 제거
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
