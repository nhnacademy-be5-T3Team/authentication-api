package com.t3t.authenticationapi.account.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {
    @PostMapping("/logout")
    public String logout(){
        System.out.println("logout");
        return "logout";
    }
}
