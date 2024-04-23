package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * Login 요청을 받는 RestController 클래스, 실제로는 UsernamePasswordAuthenticationFilter 때문에 동작하지 않는다.
 * @author joohyun1996 (이주현)
 */
@RestController
public class LoginController {
    /**
     * LoginFilter 수행시 successfulAuthentication 메소드가 수행되고 해당 메소드에서 응답이 커밋됨
     * @author joohyun1996(이주현)
     */
    @PostMapping("/login")
    public String doLogin(HttpServletRequest request){
        return "login";
    }
}
