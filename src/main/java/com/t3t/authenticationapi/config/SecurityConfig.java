package com.t3t.authenticationapi.config;

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.component.CustomAuthenticationProvider;
import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.filter.CommonExceptionFilter;
import com.t3t.authenticationapi.account.filter.CustomLogoutFilter;
import com.t3t.authenticationapi.account.filter.LoginFilter;
import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import com.t3t.authenticationapi.account.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
/**
 * Spring Security 등록을 위한 configuration 클래스
 * @author joohyun1996 (이주현)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;
    private final CustomAuthenticationProvider provider;

    @Autowired
    public void globalConfigure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(provider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    /**
     * Security Filter Chain 설정.
     * Auth-Server에서는 인증만 담당하기 때문에 다른 URL에 대해서는 설정 X
     * @param http
     * @author joohyun1996 (이주현)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests((auth) -> auth
                        .antMatchers("/login").permitAll()
                        .antMatchers("/refresh").permitAll()
                        .antMatchers("/logout").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(new CommonExceptionFilter(), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtils, tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtils, tokenService), LogoutFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
