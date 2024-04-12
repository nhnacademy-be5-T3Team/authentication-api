package com.t3t.authenticationapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.component.JWTUtils;
import com.t3t.authenticationapi.account.dto.LoginDto;
import com.t3t.authenticationapi.account.dto.UserEntity;
import com.t3t.authenticationapi.account.exception.JsonFieldNotMatchException;
import com.t3t.authenticationapi.account.service.DefaultUserDetailsService;
import com.t3t.authenticationapi.account.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class LoginFilterTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JWTUtils jwtUtils;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private DefaultUserDetailsService userDetailsService;

    @Test
    public void TestLoginSuccess() throws Exception {
        Map<String, String> testLogin = new LinkedHashMap<>();
        testLogin.put("username","user");
        testLogin.put("password","1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody = objectMapper.writeValueAsString(testLogin);
        LoginDto loginDto = objectMapper.readValue(messageBody, LoginDto.class);


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("user", "1234", null);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(auth);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("1");
        userEntity.setUsername("user");
        userEntity.setPassword("1234");
        userEntity.setRole("ROLE_USER");

        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(new CustomUserDetails(userEntity));

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername("user");
        String userId = customUserDetails.getUserId();

        String uuid = UUID.randomUUID().toString();
        String access = jwtUtils.createJwt("access", userId, "ROLE_USER", uuid, 900000l);
        String refresh = jwtUtils.createJwt("refresh", userId, "ROLE_USER", uuid,604800000l);

        Mockito.when(jwtUtils.createJwt(any(), any(), any(), any(), any())).thenReturn(access, refresh);

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageBody))
                        .andExpect(status().isOk())
                        .andReturn();

        Cookie accessCookie = mvcResult.getResponse().getCookie("access");
        Assertions.assertNotNull(accessCookie);
        Assertions.assertEquals("Bearer+" + access, accessCookie.getValue());

        Cookie refreshCookie = mvcResult.getResponse().getCookie("refresh");
        Assertions.assertNotNull(refreshCookie);
        Assertions.assertEquals(refresh, refreshCookie.getValue());
    }

    @Test
    public void TestAttemptAuthenticationFailed() throws Exception{
        Map<String, String> testLogin = new LinkedHashMap<>();
        testLogin.put("user","user");
        testLogin.put("pw","1234");

        ObjectMapper objectMapper = new ObjectMapper();
        String messageBody = objectMapper.writeValueAsString(testLogin);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageBody))
                        .andExpect(result ->
                                Assertions.assertThrows(JsonFieldNotMatchException.class, () ->
                                authenticationManager.authenticate(any())
                        ));
    }
}

