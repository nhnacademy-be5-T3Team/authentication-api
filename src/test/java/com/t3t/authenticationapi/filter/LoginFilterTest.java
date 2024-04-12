package com.t3t.authenticationapi.filter;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class LoginFilterTest {
    /*@Autowired
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
    }*/
}

