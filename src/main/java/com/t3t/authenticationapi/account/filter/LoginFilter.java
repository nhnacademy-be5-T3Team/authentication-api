package com.t3t.authenticationapi.account.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.dto.LoginDto;
import com.t3t.authenticationapi.account.entity.Refresh;
import com.t3t.authenticationapi.account.exception.JsonFieldNotMatchException;
import com.t3t.authenticationapi.account.service.TokenService;
import com.t3t.authenticationapi.member.entity.Member;
import com.t3t.authenticationapi.member.repository.MemberRepository;
import com.t3t.authenticationapi.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * 로그인 과정을 담당하는 LoginFilter
 *
 * @author joohyun1996 (이주현)
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;
    private final MemberService memberService;

    /**
     * 사용자가 입력한 login 정보를 가지고 인증 시도를 하는 메소드
     *
     * @param request,response
     * @return Authentication
     * @author joohyun1996 (이주현)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = mapper.readValue(messageBody, LoginDto.class);
        } catch (IOException e) {
            throw new JsonFieldNotMatchException(e);
        }

        String username = loginDto.getUsername();
        // 암호화된 정보로 확인
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공시 진행되는 메소드
     * 성공시 UserDetails에서 id, pw, authority 등을 꺼내 jwt 토큰 생성
     * access token은 response header에 담아 전달, refresh token은 redis에 저장
     *
     * @param request,response,chain,authentication
     * @return 200_OK, "Authorization : Bearer + accesstoken"
     * @author joohyun1996 (이주현)
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String userId = customUserDetails.getUserId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();

        String role = grantedAuthority.getAuthority();

        String uuid = UUID.randomUUID().toString();
        String access = jwtUtils.createJwt("access", userId, role, uuid, 900000l); // 15분
        String refresh = jwtUtils.createJwt("refresh", userId, role, uuid, 1800000l); // 30분

        tokenService.saveRefreshToken(Refresh.builder().token(refresh).uuid(uuid).build());

        memberService.updateMemberLoginAt(Long.parseLong(userId));

        response.addHeader("Authorization", "Bearer " + access);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * 로그인 실패시 수행되는 메소드
     *
     * @param request,response,failed
     * @return 401_Unauthorized, error message
     * @author joohyun1996 (이주현)
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String errorMessage = null;
        if (failed instanceof BadCredentialsException) {
            errorMessage = "invalid id or password";
        } else {
            errorMessage = "auth failed";
        }

        response.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        response.setStatus(401);
        response.getWriter().write(new JSONObject().put("error", errorMessage).toString());
    }
}
