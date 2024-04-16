package com.t3t.authenticationapi.account.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Logout 요청을 받는 RestController 클래스, 실제로는 LogoutFilter 때문에 동작하지 않는다.
 * @author joohyun1996 (이주현)
 */
@RestController
public class LogoutController {
    @PostMapping("/logout")
    public String logout(){
        return "logout";
    }
}
