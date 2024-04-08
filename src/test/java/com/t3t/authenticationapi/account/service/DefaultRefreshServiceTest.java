package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.exception.CookieNotExistException;
import com.t3t.authenticationapi.account.exception.TokenHasExpiredException;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class DefaultRefreshServiceTest {
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private DefaultRefreshService defaultRefreshService;

    private Cookie createCookie(String key, String value, int age, String path){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(age);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setSecure(false);
        cookie.setPath(path);

        return cookie;
    }

    @Test
    public void TestCookieCreated(){
        String key = "key";
        String value = "value";
        int age = 10;
        String path = "/";

        Cookie testCookie = createCookie(key, value, age, path);

        Assertions.assertAll(
                () -> Assertions.assertEquals(key, testCookie.getName()),
                () -> Assertions.assertEquals(value, testCookie.getValue()),
                () -> Assertions.assertEquals(10, testCookie.getMaxAge()),
                () -> Assertions.assertEquals(path, testCookie.getPath()),
                () -> Assertions.assertEquals(true, testCookie.isHttpOnly()),
                () -> Assertions.assertEquals(false, testCookie.getSecure())
        );
    }

    @Test
    public void TestSendRefreshToken(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setCookies(new Cookie("refresh", "refresh"));

        Mockito.when(jwtUtils.isExpired(anyString())).thenReturn(false);
        Mockito.when(tokenService.refreshTokenExists(anyString())).thenReturn(true);
        Mockito.when(jwtUtils.getUserName(anyString())).thenReturn("username");
        Mockito.when(jwtUtils.getRole(anyString())).thenReturn("role");
        Mockito.when(jwtUtils.createJwt(anyString(), anyString(),
                anyString(), anyString(), anyLong())).thenReturn("newToken");


        ResponseEntity<?> result = defaultRefreshService.sendRefreshToken(request, response);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertEquals("Bearer+newToken", response.getCookie("access").getValue()),
                () -> Assertions.assertEquals("newToken", response.getCookie("refresh").getValue())
        );
    }

    @Test
    public void TestSendRefreshTokenFailed_TokenNotFound(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Exception exception = Assertions.assertThrows(CookieNotExistException.class, () -> {
                    defaultRefreshService.sendRefreshToken(request, response);
        });
        Assertions.assertEquals("Cookie Not Exists", exception.getMessage());
    }

    @Test
    public void TestSendRefreshTokenFailed_TokenExpired() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setCookies(new Cookie("refresh", "refresh"));

        Mockito.when(jwtUtils.isExpired(anyString())).thenReturn(true);

        Exception exception = Assertions.assertThrows(TokenHasExpiredException.class, () -> {
            defaultRefreshService.sendRefreshToken(request, response);
        });
        Assertions.assertEquals("Token has expired", exception.getMessage());
    }

    @Test
    public void TestSendRefreshTokenFailed_TokenNotExists(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setCookies(new Cookie("refresh", "refresh"));

        Mockito.when(jwtUtils.isExpired(anyString())).thenReturn(false);
        Mockito.when(tokenService.refreshTokenExists(anyString())).thenReturn(false);

        Exception exception = Assertions.assertThrows(TokenNotExistsException.class, () -> {
            defaultRefreshService.sendRefreshToken(request,response);
        });

        Assertions.assertEquals("Send Proper Token", exception.getMessage());
    }
}