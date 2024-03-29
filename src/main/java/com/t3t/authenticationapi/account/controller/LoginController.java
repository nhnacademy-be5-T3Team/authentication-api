package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
        return "jwt token created";
    }
}
