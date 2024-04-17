package com.t3t.authenticationapi.account.service;

/*import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

import com.t3t.authenticationapi.account.auth.CustomUserDetails;
import com.t3t.authenticationapi.account.dto.UserEntityDto;
import com.t3t.authenticationapi.account.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DefaultUserDetailsServiceTest {
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultUserDetailsService defaultUserDetailsService;

    @Test
    public void TestLoadUserByUserNameSuccess(){
        String username = "user";
        UserEntityDto userEntityDto = Mockito.mock(UserEntityDto.class);

        Mockito.when(userEntityDto.getUserId()).thenReturn(String.valueOf(1l));
        Mockito.when(userEntityDto.getPassword()).thenReturn("password");
        Mockito.when(userEntityDto.getRole()).thenReturn("USER");
        Mockito.when(userEntityDto.getUsername()).thenReturn(username);

        Mockito.when(accountRepository.loadUserEntity(Mockito.any())).thenReturn(userEntityDto);
//        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn("pwEncoded");

        CustomUserDetails userDetails = (CustomUserDetails) defaultUserDetailsService.loadUserByUsername(username);

        Assertions.assertAll(
                () -> Assertions.assertEquals(String.valueOf(1l), userDetails.getUserId()),
                () -> Assertions.assertEquals("password", userDetails.getPassword())
        );
    }

    @Test
    public void TestLoadUserByUserNameFailed(){
        String username = "user";

        Mockito.when(accountRepository.loadUserEntity(Mockito.any())).thenReturn(null);

        Exception exception = Assertions.assertThrows(UsernameNotFoundException.class,
                () -> {defaultUserDetailsService.loadUserByUsername(username);
        });

        Assertions.assertEquals("User Not Found", exception.getMessage());
    }
}
