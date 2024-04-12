package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
    private final DefaultUserDetailsService service;

    public LoginController(DefaultUserDetailsService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public String doLogin(HttpServletRequest request){
        // LoginFilter 수행시 successfulAuthentication 메소드가 수행되고 해당 메소드에서 응답이 커밋됨
        return "login";
    }
}
