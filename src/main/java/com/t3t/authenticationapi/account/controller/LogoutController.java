package com.t3t.authenticationapi.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogoutController {
    @PostMapping("/logout")
    public String logout(){
        System.out.println("logout");
        return "logout";
    }

    @GetMapping("/index")
    public Map<String, String> logout2(){
        return Map.of("logout", "success");
    }
}
