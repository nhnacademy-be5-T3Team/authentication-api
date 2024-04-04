package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultRefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class RefreshController {
    private DefaultRefreshService defaultRefreshService;

    public RefreshController(DefaultRefreshService defaultRefreshService) {
        this.defaultRefreshService = defaultRefreshService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return defaultRefreshService.sendRefreshToken(request,response);
    }

    @GetMapping("/refresh") //test
    public Map<String, String> refresh2(){
        return Map.of("result", "success");
    }
}
