package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RefreshController {
    private final DefaultRefreshService defaultRefreshService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return defaultRefreshService.sendRefreshToken(request,response);
    }

    @GetMapping("/refresh")
    public Map<String, String> ttt(){
        return Map.of("refresh", "success");
    }
}
