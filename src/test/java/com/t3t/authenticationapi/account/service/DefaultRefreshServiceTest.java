package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class DefaultRefreshServiceTest {
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private DefaultRefreshService defaultRefreshService;

    @Test
    public void TestRefreshSuccess_accessExpired(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        String access = "Bearer access";
        String newAccess = "newAccess";
        String refresh = "refresh";
        String uuid = "uuid";

        Mockito.when(request.getHeader("Authorization")).thenReturn(access);
        Mockito.when(tokenService.findRefreshByUUID(Mockito.any())).thenAnswer(new Answer<String>() {
            private int count = 0;

            public String answer(InvocationOnMock invocation) {
                if (count++ == 0) {
                    Claims claims = Jwts.claims();
                    claims.put("uuid", uuid);
                    throw new ExpiredJwtException(null, claims, "Token expired");
                }
                return refresh;
            }
        });
        Mockito.when(jwtUtils.isExpired(Mockito.any())).thenReturn(false);

        Mockito.when(jwtUtils.getUserName(refresh)).thenReturn("1");
        Mockito.when(jwtUtils.getRole(refresh)).thenReturn("ROLE_USER");
        Mockito.when(jwtUtils.createJwt("access", jwtUtils.getUserName(refresh),
                jwtUtils.getRole(refresh), jwtUtils.getUUID(refresh), 900000l)).thenReturn(newAccess);

        ResponseEntity<?> result = defaultRefreshService.refresh(request, response);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertEquals("Bearer newAccess", response.getHeader("Authorization"))
        );
    }

    @Test
    public void TestRefreshSuccess_accessNotExpired(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        String access = "Bearer access";
        String newAccess = "newAccess";
        String refresh = "refresh";

        Mockito.when(request.getHeader("Authorization")).thenReturn(access);
        Mockito.when(tokenService.findRefreshByUUID(Mockito.any())).thenReturn(refresh);

        Mockito.when(jwtUtils.getUserName(Mockito.any())).thenReturn("1");
        Mockito.when(jwtUtils.getRole(Mockito.any())).thenReturn("ROLE_USER");
        Mockito.when(jwtUtils.getUUID(Mockito.any())).thenReturn("uuid");

        Mockito.when(jwtUtils.checkReIssue(Mockito.any())).thenReturn(true);
        Mockito.when(tokenService.refreshTokenExists(Mockito.any())).thenReturn(true);

        Mockito.when(jwtUtils.createJwt("access", jwtUtils.getUserName(refresh),
                jwtUtils.getRole(refresh), jwtUtils.getUUID(refresh), 900000l)).thenReturn(newAccess);

        ResponseEntity<?> result = defaultRefreshService.refresh(request, response);

        Assertions.assertAll(
                () -> Assertions.assertEquals(HttpStatus.OK, result.getStatusCode()),
                () -> Assertions.assertEquals("Bearer newAccess", response.getHeader("Authorization"))
        );
    }

    @Test
    public void TestRefreshFailed_AuthorityNotFound(){
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        String header = request.getHeader("Authorization");
        Mockito.when(header == null).thenReturn(null);
        Exception exception = Assertions.assertThrows(TokenNotExistsException.class, () -> {
            defaultRefreshService.refresh(request, response);
        });

        Assertions.assertEquals("Access Token Not Exists", exception.getMessage());
    }

    @Test
    public void TestRefreshFailed_BothTokenNotFound() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validAccessToken");

        Mockito.when(jwtUtils.checkReIssue(Mockito.anyString())).thenReturn(false);
        ResponseEntity<?> responseEntity = defaultRefreshService.refresh(request, response);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}