package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class RefreshController {
    private final DefaultRefreshService defaultRefreshService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return defaultRefreshService.refresh(request,response);
    }
}
