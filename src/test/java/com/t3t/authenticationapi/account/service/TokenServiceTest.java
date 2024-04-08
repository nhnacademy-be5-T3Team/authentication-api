package com.t3t.authenticationapi.account.service;

import com.t3t.authenticationapi.account.entity.Refresh;
import com.t3t.authenticationapi.account.exception.TokenAlreadyExistsException;
import com.t3t.authenticationapi.account.exception.TokenNotExistsException;
import com.t3t.authenticationapi.account.repository.BlackListRepository;
import com.t3t.authenticationapi.account.repository.RefreshRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private RefreshRepository refreshRepository;
    @Mock
    private BlackListRepository blackListRepository;

    @InjectMocks
    private TokenService tokenService;


    @Test
    public void testSaveRefreshTokenSuccess(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(false);
        tokenService.saveRefreshToken(refresh);
        Mockito.verify(refreshRepository, Mockito.times(1)).save(refresh);
    }

    @Test
    public void testSaveRefreshTokenFail(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(true);
        Assertions.assertThatThrownBy(()-> tokenService.saveRefreshToken(refresh)).isInstanceOf(TokenAlreadyExistsException.class).hasMessage("Token Already Exists");
    }

    @Test
    public void testRemoveRefreshTokenSuccess(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(refreshRepository.findById(Mockito.any())).thenReturn(Optional.of(refresh));

        tokenService.removeRefreshToken(refresh.getToken());

        Mockito.verify(refreshRepository,Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    public void testRemoveRefreshTokenFailed(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(false);
        Assertions.assertThatThrownBy(()->tokenService.removeRefreshToken(refresh.getToken())).isInstanceOf(TokenNotExistsException.class);
    }

    @Test
    public void testRemoveRefreshTokenByUUIDSuccess(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.findByUuid(Mockito.any())).thenReturn(Optional.of(refresh));

        tokenService.removeRefreshTokenByUUID(refresh.getUuid());

        Mockito.verify(refreshRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    public void testRemoveRefreshTokenByUUISFailed(){
        Refresh refresh = Refresh.builder().token("token").uuid("1t2").build();
        Mockito.when(refreshRepository.findByUuid(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tokenService.removeRefreshTokenByUUID(refresh.getUuid())).isInstanceOf(TokenNotExistsException.class);
    }

    @Test
    public void saveBlackListTokenSuccess(){
        String blackList = "black";

        Mockito.when(blackListRepository.existsById(Mockito.any())).thenReturn(false);
        tokenService.saveBlackListToken(blackList);

        Mockito.verify(blackListRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void saveBlackListTokenFailed(){
        String blackList = "black";

        Mockito.when(blackListRepository.existsById(Mockito.any())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> tokenService.saveBlackListToken(Mockito.any())).isInstanceOf(TokenAlreadyExistsException.class);
    }

    @Test
    public void refreshTokenExistsSuccess(){
        String refresh = "refresh";

        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(true);

        tokenService.refreshTokenExists(refresh);

        Mockito.verify(refreshRepository, Mockito.times(1)).existsById(Mockito.any());
    }
    @Test
    public void refreshTokenExistsFailed(){
        String refresh = "refresh";

        Mockito.when(refreshRepository.existsById(Mockito.any())).thenReturn(false);
        tokenService.refreshTokenExists(refresh);
        Mockito.verify(refreshRepository, Mockito.times(1)).existsById(Mockito.any());
    }
}