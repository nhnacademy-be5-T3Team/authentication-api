package com.t3t.authenticationapi.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.dto.LoginDto;
import com.t3t.authenticationapi.account.entity.Refresh;
import com.t3t.authenticationapi.account.exception.JsonFieldNotMatchException;
import com.t3t.authenticationapi.account.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = mapper.readValue(messageBody, LoginDto.class);
        }catch (IOException e){
            throw new JsonFieldNotMatchException(e);
        }

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)  {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String userId = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends  GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();

        String role = grantedAuthority.getAuthority();

        String uuid = UUID.randomUUID().toString();
        String access = jwtUtils.createJwt("access", userId, role, uuid, 900000l); // 15분
        String refresh = jwtUtils.createJwt("refresh", userId, role, uuid,604800000l); // 1주

        tokenService.saveRefreshToken(Refresh.builder().refresh(refresh).uuid(uuid).build());

        response.addCookie(createCookie("access", "Bearer+" + access, 60*15, "/"));
//        response.addCookie(createCookie("access", "Bearer+" + access, 1, "/"));
        response.addCookie(createCookie("refresh", refresh, 60*60*24*7, "/refresh"));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMessage = null;
        if(failed instanceof BadCredentialsException){
            errorMessage = "invalid id or password";
        }else{
            errorMessage = "auth failed";
        }

        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        response.setStatus(401);
        response.getWriter().write(new JSONObject().put("error", errorMessage).toString());
    }

    private Cookie createCookie(String key, String value, int age, String path){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(age); //1주일 동안 유효
        cookie.setHttpOnly(true); // js 접근불가
        cookie.setDomain("www.t3t.shop"); // domain 설정
//        cookie.setDomain("localhost");
        cookie.setSecure(false); // https 설정
        cookie.setPath(path);

        return cookie;
    }
}
