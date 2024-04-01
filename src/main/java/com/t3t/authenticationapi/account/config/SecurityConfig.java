package com.t3t.authenticationapi.account.config;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.filter.CustomLogoutFilter;
import com.t3t.authenticationapi.account.filter.JWTFilter;
import com.t3t.authenticationapi.account.filter.LoginFilter;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtils jwtUtils;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtils jwtUtils) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtils = jwtUtils;
    }

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
                        .antMatchers("/logout").authenticated()
                        //.antMatchers("/logout").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTFilter(jwtUtils), LoginFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtils), LogoutFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
