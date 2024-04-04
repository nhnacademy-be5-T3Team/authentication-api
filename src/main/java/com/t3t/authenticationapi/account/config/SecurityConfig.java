package com.t3t.authenticationapi.account.config;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.filter.LogoutFilter;
import com.t3t.authenticationapi.account.filter.LoginFilter;
import com.t3t.authenticationapi.account.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtils jwtUtils;
    private final TokenService tokenService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable() // disable
                .authorizeRequests((auth) -> auth
                        .antMatchers("/login").permitAll()
                        .antMatchers("/refresh").permitAll()
                        .antMatchers("/index").permitAll()
                        .antMatchers("/logout").authenticated()
                        .anyRequest().authenticated())
                //.addFilterBefore(new JWTFilter(jwtUtils), LoginFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout") // logout 담당 url
                        .logoutSuccessUrl("/index")) // logout 성공시 redirect 할 url
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtils, tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LogoutFilter(jwtUtils, tokenService), org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
