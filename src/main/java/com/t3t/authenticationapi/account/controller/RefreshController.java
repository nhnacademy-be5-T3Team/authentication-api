package com.t3t.authenticationapi.account.controller;

import com.t3t.authenticationapi.account.service.DefaultRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * access token의 재발급을 담당하는 rest controller
 * @author joohyun1996 (이주현)
 */
@RestController
@RequiredArgsConstructor
public class RefreshController {
    private final DefaultRefreshService defaultRefreshService;
    /**
     * 토큰 재발급을 위한 api
     * @param request,response
     * @return 200 OK - access token 재발급
     * 403 Forbidden - access & refresh 만료시
     * @author joohyun1996 (이주현)
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        return defaultRefreshService.refresh(request,response);
    }
}
