package com.t3t.authenticationapi.account.component;

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntity;
import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final DefaultUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        String dbPassword = userDetails.getPassword();
        if(!bCryptPasswordEncoder().matches(password,dbPassword)){
            throw new BadCredentialsException("id, pw not match");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
